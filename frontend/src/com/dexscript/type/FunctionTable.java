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
        SubstituteConst substituteConst = new SubstituteConst(ivc.posArgs());
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

    // implement polymorphism: choose candidates statically
    // in runtime, invocation will dispatch candidate based on actual type
    public Invoked tryInvoke(Invocation ivc, List<DType> args) {
        String funcName = ivc.funcName();
        pullFromProviders();
        List<FunctionType> functions = defined.get(funcName);
        if (functions == null) {
            return new Invoked();
        }
        Invoked invoked = new Invoked();
        List<FunctionSig.Invoked> potentialCandidates = new ArrayList<>();
        for (int i = 0; i < functions.size(); i++) {
            FunctionType func = functions.get(i);
            if (invoked.match != null) {
                invoked.skippeds.add(func);
                continue;
            }
            MapNamedArgs mapNamedArgs = MapNamedArgs.$(func, args, ivc.namedArgs());
            if (mapNamedArgs == null) {
                invoked.skippeds.add(func);
                continue;
            }
            FunctionSig.Invoked candidate = func.sig().invoke(ivc.typeArgs(), mapNamedArgs.args, ivc.retHint());
            if (!candidate.success()) {
                invoked.failures.add(candidate);
                continue;
            }
            potentialCandidates.add(candidate);
            if (!candidate.needRuntimeCheck()) {
                invoked.args = mapNamedArgs.args;
                invoked.namedArgsMapping = mapNamedArgs.mapping;
                invoked.match = candidate;
            }
        }
        if (invoked.match != null) {
            for (FunctionSig.Invoked candidate : potentialCandidates) {
                if (ivc.requireImpl() && !candidate.function().hasImpl()) {
                    // no impl
                    invoked.ignoreds.add(candidate);
                } else if (!IsAssignable.$(candidate.function(), invoked.match.function())) {
                    // not overriding the match
                    invoked.ignoreds.add(candidate);
                } else {
                    invoked.candidates.add(candidate);
                }
            }
        } else {
            invoked.ignoreds.addAll(potentialCandidates);
        }
        return invoked;
    }

    public void lazyDefine(FunctionsType provider) {
        providers.add(provider);
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

    private boolean isDefined(IsAssignable ctx, FunctionType target) {
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
}
