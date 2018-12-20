package com.dexscript.type;

class FloatConstType implements DType {

    private final TypeSystem ts;
    private final String val;

    public FloatConstType(TypeSystem ts, String val) {
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
}
