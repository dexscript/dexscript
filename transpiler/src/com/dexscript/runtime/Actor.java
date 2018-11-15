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
}
