package com.dexscript.type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FunctionTable {

    public interface OnInvocationFilteredFunction {
        void handle(FunctionType func, Invocation ivc);
    }

    public static final OnInvocationFilteredFunction ON_INVOCATION_FILTERED_FUNCTION = (func, ivc) -> {
    };

    public interface OnFunctionDefined {
        void handle(FunctionType func);
    }

    public static OnFunctionDefined ON_FUNCTION_DEFINED = func -> {
    };

    private final Map<String, List<FunctionType>> defined = new HashMap<>();
    private final List<FunctionsProvider> providers = new ArrayList<>();

    public void define(FunctionType func) {
        List<FunctionType> functions = defined.computeIfAbsent(func.name(), k -> new ArrayList<>());
        functions.add(func);
        ON_FUNCTION_DEFINED.handle(func);
    }

    public List<FunctionSig.Invoked> invoke(TypeTable typeTable, Invocation ivc) {
        String funcName = ivc.funcName();
        pullFromProviders();
        List<FunctionType> functions = defined.get(funcName);
        if (functions == null) {
            return new ArrayList<>();
        }
        List<FunctionSig.Invoked> invokeds = new ArrayList<>();
        for (int i = functions.size() - 1; i >= 0; i--) {
            FunctionType func = functions.get(i);
            FunctionSig.Invoked invoked = func.sig().invoke(typeTable, ivc);
            if (!invoked.compatible()) {
                ON_INVOCATION_FILTERED_FUNCTION.handle(func, ivc);
                continue;
            }
            invokeds.add(invoked);
            if (!invoked.needRuntimeCheck()) {
                return invokeds;
            }
        }
        return invokeds;
    }

    public void lazyDefine(FunctionsProvider provider) {
        providers.add(provider);
    }

    public boolean isDefined(TypeComparisonContext ctx, FunctionType that) {
        pullFromProviders();
        List<FunctionType> functions = defined.get(that.name());
        if (functions == null) {
            return false;
        }
        if (ctx.shouldLog()) {
            ctx.log(">>> check if " + that + " defined or not");
        }
        TypeComparisonContext staging = new TypeComparisonContext(ctx);
        for (FunctionType function : functions) {
            if (ctx.isUndefined(function)) {
                continue;
            }
            if (that.isAssignableFrom(staging, function)) {
                staging.commit();
                if (ctx.shouldLog()) {
                    ctx.log("<<< " + that + " is defined");
                }
                return true;
            } else {
                staging.rollback();
            }
        }
        if (ctx.shouldLog()) {
            ctx.log("<<< " + that + " is not defined");
        }
        return false;
    }

    private void pullFromProviders() {
        while (!(providers.isEmpty())) {
            ArrayList<FunctionsProvider> toPull = new ArrayList<>(providers);
            providers.clear();
            for (FunctionsProvider provider : toPull) {
                for (FunctionType function : provider.functions()) {
                    define(function);
                }
            }
        }
    }

}
