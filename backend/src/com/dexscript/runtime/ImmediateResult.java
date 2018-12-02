package com.dexscript.runtime;

public class ImmediateResult implements Result {

    private final Object value;

    public ImmediateResult(Object value) {
        this.value = value;
    }

    @Override
    public boolean finished() {
        return true;
    }

    @Override
    public Object value() {
        return value;
    }
}
