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

    static DType $(TypeSystem ts, String funcName, List<DType> typeArgs, List<DType> args, DType retHint) {
        Invoked invoked = ts.invoke(new Invocation(funcName, typeArgs, args, retHint));
        if (invoked.successes().size() == 0) {
            Events.ON_MISSING_FUNCTION.handle(ts, funcName, args);
            return ts.UNDEFINED;
        }
        return ResolveReturnType.$(invoked);
    }

    static DType $(Invoked invoked) {
        List<FunctionSig.Invoked> successes = invoked.successes();
        if (successes.size() == 1) {
            return successes.get(0).function().ret();
        }
        DType ret = successes.get(0).function().ret();
        for (int i = 1; i < successes.size(); i++) {
            ret = ret.union(successes.get(i).function().ret());
        }
        return ret;
    }
}
