package com.dexscript.type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FunctionTable {

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

    public Invoked invoke(Invocation ivc) {
        SubstituteConst substituteConst = new SubstituteConst(ivc.args());
        Invoked invoked = null;
        while (substituteConst.hasNext()) {
            List<DType> args = substituteConst.next();
            invoked = tryInvoke(ivc, args);
            if (!invoked.candidates.isEmpty()) {
                return invoked;
            }
        }
        return invoked;
    }

    public Invoked tryInvoke(Invocation ivc, List<DType> args) {
        String funcName = ivc.funcName();
        pullFromProviders();
        List<FunctionType> functions = defined.get(funcName);
        if (functions == null) {
            return new Invoked(args);
        }
        boolean alreadyMatched = false;
        Invoked invoked = new Invoked(args);
        for (int i = 0; i < functions.size(); i++) {
            FunctionType func = functions.get(i);
            if (alreadyMatched) {
                invoked.skippeds.add(func);
                continue;
            }
            FunctionSig.Invoked candidate = func.sig().invoke(ivc, args);
            if (!candidate.success()) {
                invoked.failures.add(candidate);
                continue;
            }
            if (ivc.requireImpl() && !candidate.function().hasImpl()) {
                invoked.failures.add(candidate);
                continue;
            }
            invoked.candidates.add(candidate);
            if (!candidate.needRuntimeCheck()) {
                alreadyMatched = true;
            }
        }
        return invoked;
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
