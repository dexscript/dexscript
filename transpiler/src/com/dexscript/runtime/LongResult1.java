package com.dexscript.runtime;

public class LongResult1 implements Result {

    private final long result1__;

    public LongResult1(long result1__) {
        this.result1__ = result1__;
    }

    @Override
    public boolean isResultReady() {
        return true;
    }

    @Override
    public Object result1__() {
        return result1__;
    }

    @Override
    public long result1__long() {
        return result1__;
    }
}
