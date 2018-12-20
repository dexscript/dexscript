package com.dexscript.type;

public class StringConstType implements DType {

    private final TypeSystem ts;
    private final String val;

    public StringConstType(TypeSystem ts, String val) {
        this.ts = ts;
        this.val = val;
    }

    @Override
    public boolean _isAssignable(IsAssignable ctx, DType that) {
        return false;
    }

    @Override
    public TypeSystem typeSystem() {
        return ts;
    }

    public String constValue() {
        return val;
    }

    @Override
    public String toString() {
        return "const'" + val;
    }
}
