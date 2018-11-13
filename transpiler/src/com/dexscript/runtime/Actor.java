package com.dexscript.runtime;

public class Actor implements Result {

    private boolean isResultReady;

    protected void finish() {
        if (isResultReady) {
            throw new IllegalStateException("has already been finished");
        }
        isResultReady = true;
    }

    @Override
    public boolean isResultReady() {
        return isResultReady;
    }

    @Override
    public Object result1__() {
        return null;
    }

    @Override
    public long result1__long() {
        return 0;
    }
}
