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
    private final List<FunctionsType> providers = new ArrayList<>();

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
        for (int i = 0; i < functions.size(); i++) {
            FunctionType func = functions.get(i);
            FunctionSig.Invoked invoked = func.sig().invoke(ivc);
            if (!invoked.success()) {
                ON_INVOCATION_FILTERED_FUNCTION.handle(func, ivc);
                continue;
            }
            if (ivc.requireImpl() && !invoked.function().hasImpl()) {
                continue;
            }
            invokeds.add(invoked);
            if (!invoked.needRuntimeCheck()) {
                return invokeds;
            }
        }
        return invokeds;
    }

    public void lazyDefine(FunctionsType provider) {
        providers.add(provider);
    }

    public boolean isDefined(IsAssignable ctx, FunctionType target) {
        pullFromProviders();
        List<FunctionType> candidates = defined.get(target.name());
        if (candidates == null) {
            return false;
        }
        for (FunctionType candidate : candidates) {
            if (!ctx.isAvailable(candidate)) {
                ctx.addLog("member " + target + " filter not available candidate", "candidate", candidate);
                continue;
            }
            if (new IsAssignable(ctx, "member " + target + " candidate", target, candidate).result()) {
                return true;
            }
        }
        ctx.addLog("member " + target + " is not defined", "candidates_count", candidates.size());
        return false;
    }

    private void pullFromProviders() {
        while (!(providers.isEmpty())) {
            ArrayList<FunctionsType> toPull = new ArrayList<>(providers);
            providers.clear();
            for (FunctionsType provider : toPull) {
                provider.functions();
            }
        }
    }

    public boolean isAssignable(IsAssignable ctx, FunctionsType to, DType from) {
        if (from instanceof FunctionsType) {
            for (FunctionType member : ((FunctionsType) from).functions()) {
                ctx.makeAvailable(member);
            }
        }
        ctx.substitute(from, to);
        for (FunctionType member : to.functions()) {
            if (!isDefined(ctx, member)) {
                return false;
            }
        }
        ctx.unsubstitute(from);
        return true;
    }

}
