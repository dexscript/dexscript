package com.dexscript.runtime;

public class Result1Impl extends Result implements Result1 {

    private final Object result1__;

    public Result1Impl(Object result1__) {
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
}
