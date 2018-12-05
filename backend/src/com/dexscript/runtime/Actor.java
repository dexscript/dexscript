package com.dexscript.runtime;

public abstract class Actor implements Result {

    private boolean finished;
    private Object ret;

    @Override
    public boolean finished() {
        return finished;
    }

    protected final void finish(Object ret) {
        finished = true;
        this.ret = ret;
    }

    @Override
    public Object value() {
        return ret;
    }
}
