package com.dexscript.runtime;

public class ObjectResult1 implements Result {

    private final Object result1__;

    public ObjectResult1(Object result1__) {
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
        return (Long)result1__;
    }
}
