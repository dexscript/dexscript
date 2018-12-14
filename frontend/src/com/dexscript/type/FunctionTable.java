package com.dexscript.type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FunctionTable {

    public interface OnInvocationFilteredFunction {
        void handle(String funcName, FunctionType func, List<Type> typeArgs, List<Type> args);
    }

    public static final OnInvocationFilteredFunction ON_INVOCATION_FILTERED_FUNCTION = (funcName, func, typeArgs, args) -> {
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

    public List<FunctionType.Invoked> invoke(
            TypeTable typeTable, String funcName,
            List<Type> typeArgs, List<Type> args, Type retHint) {
        pullFromProviders();
        List<FunctionType> functions = defined.get(funcName);
        if (functions == null) {
            return new ArrayList<>();
        }
        List<FunctionType.Invoked> invokeds = new ArrayList<>();
        for (FunctionType function : functions) {
            Type ret = function.sig().invoke(typeTable, typeArgs, args, retHint);
            if (BuiltinTypes.UNDEFINED.equals(ret)) {
                ON_INVOCATION_FILTERED_FUNCTION.handle(funcName, function, typeArgs, args);
                continue;
            }
            FunctionType.Invoked invoked = new FunctionType.Invoked(function, ret);
            invokeds.add(invoked);
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
