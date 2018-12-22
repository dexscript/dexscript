package com.dexscript.type;

import java.util.List;

public interface ResolveReturnType {

    interface OnMissingFunction {
        void handle(TypeSystem ts, String funcName, List<DType> args);
    }

    class Events {
        public static OnMissingFunction ON_MISSING_FUNCTION = (ts, funcName, args) -> {
            System.out.println("missing function: " + funcName + " with " + args);
        };
    }

    // TODO: merge this into invoked
    static DType $(TypeSystem ts, String funcName, List<DType> typeArgs, List<DType> args, DType retHint) {
        Invoked invoked = ts.invoke(new Invocation(funcName, typeArgs, args, null, retHint));
        if (invoked.candidates.size() == 0) {
            Events.ON_MISSING_FUNCTION.handle(ts, funcName, args);
            return ts.UNDEFINED;
        }
        return ResolveReturnType.$(invoked);
    }

    static DType $(Invoked invoked) {
        List<FunctionSig.Invoked> candidates = invoked.candidates;
        if (candidates.size() == 1) {
            return candidates.get(0).function().ret();
        }
        DType ret = candidates.get(0).function().ret();
        for (int i = 1; i < candidates.size(); i++) {
            ret = ret.union(candidates.get(i).function().ret());
        }
        return ret;
    }
}
