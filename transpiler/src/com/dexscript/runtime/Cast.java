package com.dexscript.runtime;

public class Cast {

    public static Result Cast__(Object castFrom, Object castToType) {
        if (can__castLongToInt(castFrom, castToType)) {
            return castLongToInt(castFrom, castToType);
        }
        throw new UnsupportedOperationException("not implemented");
    }

    public static boolean can__castLongToInt(Object castFrom, Object castToType) {
        return castFrom.getClass() == Long.class && castToType == Integer.class;
    }

    private static Result castLongToInt(Object castFrom, Object castToType) {
        return new Result1Impl(((Long)castFrom).intValue());
    }
}
