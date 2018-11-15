package com.dexscript.runtime;

public class Math {

    public static Result Add__(Object left, Object right) {
        if (can__addLong(left, right)) {
            return addLong(left, right);
        }
        throw new UnsupportedOperationException("not implemented");
    }

    public static Result addLong(Object left, Object right) {
        return new Result1Impl((Long)left + (Long)right);
    }

    public static boolean can__addLong(Object left, Object right) {
        return left.getClass() == Long.class && right.getClass() == Long.class;
    }
}
