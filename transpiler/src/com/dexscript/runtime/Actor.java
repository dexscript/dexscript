package com.dexscript.runtime;

public class Actor {

    private boolean isFinished;

    protected void finish() {
        if (isFinished) {
            throw new IllegalStateException("has already been finished");
        }
        isFinished = true;
    }

    public boolean isFinished() {
        return isFinished;
    }
}
