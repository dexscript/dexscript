package com.dexscript.type;


public class IsAssignable {

    private boolean result;

    public IsAssignable(DType to, DType from) {
        result = to._isSubType(this, from);
    }

    public boolean result() {
        return result;
    }
}
