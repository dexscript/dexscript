package com.dexscript.type.core;

public class VoidType implements DType {

    private final TypeSystem ts;

    public VoidType(TypeSystem ts) {
        this.ts = ts;
    }

    @Override
    public boolean _isAssignable(IsAssignable ctx, DType that) {
        return that instanceof VoidType;
    }

    @Override
    public TypeSystem typeSystem() {
        return ts;
    }

    @Override
    public String toString() {
        return "void";
    }
}
