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

    public List<FunctionSig.Invoked> invoke(Invocation ivc) {
        String funcName = ivc.funcName();
        pullFromProviders();
        List<FunctionType> functions = defined.get(funcName);
        if (functions == null) {
            return new ArrayList<>();
        }
        List<FunctionSig.Invoked> invokeds = new ArrayList<>();
        for (int i = functions.size() - 1; i >= 0; i--) {
            FunctionType func = functions.get(i);
            FunctionSig.Invoked invoked = func.sig().invoke(ivc);
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
        TypeComparisonContext staging = new TypeComparisonContext(ctx, true);
        for (FunctionType function : functions) {
            if (!ctx.isAvailable(function)) {
                continue;
            }
            staging.compare(that, function);
            if (that.isAssignableFrom(staging, function)) {
                staging.commit();
                if (ctx.shouldLog()) {
                    ctx.log("<<< " + that + " is defined");
                }
                return true;
            } else {
                staging = new TypeComparisonContext(ctx, true);
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

    public boolean isSubType(TypeComparisonContext ctx, FunctionsProvider to, DType from) {
        if (ctx.shouldLog()) {
            ctx.log(">>> check " + to + " is assignable from " + from);
        }
        TypeComparisonContext subCtx = new TypeComparisonContext(ctx)
                .compare(to, from);
        subCtx.putSubstituted(from, to);
        if (from instanceof FunctionsProvider) {
            for (FunctionType member : ((FunctionsProvider) from).functions()) {
                subCtx.makeAvailable(member);
            }
        }
        for (FunctionType member : to.functions()) {
            subCtx.makeUnavailable(member);
        }
        for (FunctionType member : to.functions()) {
            if (!isDefined(subCtx, member)) {
                if (ctx.shouldLog()) {
                    ctx.log("<<< " + to + " is not assignable from " + from + " because missing " + member);
                }
                return false;
            }
        }
        subCtx.commit();
        if (ctx.shouldLog()) {
            ctx.log("<<< " + to + " is assignable from " + from);
        }
        return true;
    }

}
