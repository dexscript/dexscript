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
        List<FunctionSig.Invoked> invokeds = ts.invoke(new Invocation(funcName, typeArgs, args, retHint));
        if (invokeds.size() == 0) {
            Events.ON_MISSING_FUNCTION.handle(ts, funcName, args);
            return ts.UNDEFINED;
        }
        return $(invokeds);
    }

    static DType $(List<FunctionSig.Invoked> invokeds) {
        if (invokeds.size() == 1) {
            return invokeds.get(0).function().ret();
        }
        DType ret = invokeds.get(0).function().ret();
        for (int i = 1; i < invokeds.size(); i++) {
            ret = ret.union(invokeds.get(i).function().ret());
        }
        return ret;
    }
}
