package com.dexscript.runtime;

public class Math {

    public static Result add(long left, long right) {
        return new LongResult1(left + right);
    }
}
