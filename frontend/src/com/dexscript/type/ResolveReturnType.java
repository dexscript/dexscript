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
        List<FunctionType.Invoked> functions = ts.invoke(funcName, typeArgs, args, retHint);
        if (functions.size() == 0) {
            Events.ON_MISSING_FUNCTION.handle(ts, funcName, args);
            return BuiltinTypes.UNDEFINED;
        }
        Type ret = functions.get(0).ret();
        for (int i = 1; i < functions.size(); i++) {
            ret = ret.union(functions.get(i).ret());
        }
        return ret;
    }

    static Type $(List<FunctionType.Invoked> functions) {
        Type ret = functions.get(0).ret();
        for (int i = 1; i < functions.size(); i++) {
            ret = ret.union(functions.get(i).ret());
        }
        return ret;
    }
}
