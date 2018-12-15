package com.dexscript.type;

import java.util.List;

public interface ResolveReturnType {

    interface OnMissingFunction {
        void handle(TypeSystem ts, String funcName, List<Type> args);
    }

    class Events {
        public static OnMissingFunction ON_MISSING_FUNCTION = (ts, funcName, args) -> {
            System.out.println("missing function: " + funcName + " with " + args);
        };
    }

    static Type $(TypeSystem ts, String funcName, List<Type> typeArgs, List<Type> args, Type retHint) {
        List<FunctionType.Invoked> invokeds = ts.invoke(new Invocation(funcName, typeArgs, args, retHint));
        if (invokeds.size() == 0) {
            Events.ON_MISSING_FUNCTION.handle(ts, funcName, args);
            return BuiltinTypes.UNDEFINED;
        }
        return $(invokeds);
    }

    static Type $(List<FunctionType.Invoked> invokeds) {
        if (invokeds.size() == 1) {
            return invokeds.get(0).ret();
        }
        Type ret = invokeds.get(0).ret();
        for (int i = 1; i < invokeds.size(); i++) {
            ret = ret.union(invokeds.get(i).ret());
        }
        return ret;
    }
}
