package com.dexscript.runtime;

public class Math {

    public static Result Add__(Object left, Object right) {
        if (can__addLong(left, right)) {
            return addLong(left, right);
        }
        if (can__addInt(left, right)) {
            return addInt(left, right);
        }
        DexScriptException.reportMissingImplementation("Add__", left, right);
        throw new UnsupportedOperationException("not implemented");
    }

    public static Result addLong(Object left, Object right) {
        return new Result1Impl((Long)left + (Long)right);
    }

    public static boolean can__addLong(Object left, Object right) {
        return left.getClass() == Long.class && right.getClass() == Long.class;
    }

    public static Result addInt(Object left, Object right) {
        return new Result1Impl((Integer)left + (Integer)right);
    }

    public static boolean can__addInt(Object left, Object right) {
        return left.getClass() == Integer.class && right.getClass() == Integer.class;
    }
}
