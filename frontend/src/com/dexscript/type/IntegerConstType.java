package com.dexscript.type;

public class IntegerConstType implements DType {

    private final TypeSystem ts;
    private final String val;

    public IntegerConstType(TypeSystem ts, String val) {
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

    @Override
    public String toString() {
        return "const'" + val;
    }

    public String constValue() {
        return val;
    }
}
