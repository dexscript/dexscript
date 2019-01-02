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

    public Dispatched dispatch(Invocation ivc) {
        SubstituteConst substituteConst = new SubstituteConst(ivc.posArgs(), ivc.namedArgs());
        Dispatched dispatched = null;
        while (substituteConst.hasNext()) {
            SubstituteConst.Combination combination = substituteConst.next();
            dispatched = tryDispatch(ivc, combination.posArgs, combination.namedArgs);
            if (!dispatched.candidates.isEmpty()) {
                return dispatched;
            }
        }
        return dispatched;
    }

    // implement polymorphism: choose candidates statically
    // in runtime, invocation will dispatch candidate based on actual type
    public Dispatched tryDispatch(Invocation ivc, List<DType> args, List<NamedArg> namedArgs) {
        String funcName = ivc.funcName();
        pullFromProviders();
        List<FunctionType> functions = defined.get(funcName);
        if (functions == null) {
            return new Dispatched();
        }
        Dispatched dispatched = new Dispatched();
        List<FunctionSig.Invoked> potentialCandidates = new ArrayList<>();
        for (int i = 0; i < functions.size(); i++) {
            FunctionType func = functions.get(i);
            if (!func.hasImpl() && !func.isGlobalSPI() && !ivc.canProvide(func)) {
                continue;
            }
            if (dispatched.match != null && dispatched.match.func().hasImpl()) {
                dispatched.skippeds.add(func);
                continue;
            }
            MapNamedArgs mapNamedArgs = MapNamedArgs.$(func, args, namedArgs);
            if (mapNamedArgs == null) {
                dispatched.skippeds.add(func);
                continue;
            }
            FunctionSig.Invoked candidate = func.sig().invoke(ivc.typeArgs(), mapNamedArgs.args, ivc.context(), ivc.retHint());
            if (!candidate.success()) {
                dispatched.failures.add(candidate);
                continue;
            }
            if (dispatched.match == null || candidate.func().hasImpl()) {
                potentialCandidates.add(candidate);
            }
            if (dispatched.match == null && !candidate.needRuntimeCheck()) {
                dispatched.args = mapNamedArgs.args;
                dispatched.namedArgsMapping = mapNamedArgs.mapping;
                dispatched.match = candidate;
            }
        }
        if (dispatched.match != null) {
            for (FunctionSig.Invoked candidate : potentialCandidates) {
                if (ivc.requireImpl() && !candidate.func().hasImpl()) {
                    // no impl
                    dispatched.ignoreds.add(candidate);
                } else if (!IsAssignable.$(candidate.func(), dispatched.match.func())) {
                    // not overriding the match
                    dispatched.ignoreds.add(candidate);
                } else {
                    dispatched.candidates.add(candidate);
                }
            }
        } else {
            dispatched.ignoreds.addAll(potentialCandidates);
        }
        dispatched.ret = unionRet(dispatched.candidates);
        return dispatched;
    }

    private DType unionRet(List<FunctionSig.Invoked> candidates) {
        if (candidates.isEmpty()) {
            return null;
        }
        if (candidates.size() == 1) {
            return candidates.get(0).func().ret();
        }
        DType ret = candidates.get(0).func().ret();
        for (int i = 1; i < candidates.size(); i++) {
            ret = ret.union(candidates.get(i).func().ret());
        }
        return ret;
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
