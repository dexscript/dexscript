package com.dexscript.type;

public class UndefinedType implements DType {

    private final TypeSystem ts;

    public UndefinedType(TypeSystem ts) {
        this.ts = ts;
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
        return "undefined";
    }
}
