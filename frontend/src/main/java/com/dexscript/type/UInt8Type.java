package com.dexscript.type;

public class UInt8Type implements DType {

    private final TypeSystem ts;

    public UInt8Type(TypeSystem ts) {
        this.ts = ts;
    }

    @Override
    public String toString() {
        return "uint8";
    }

    @Override
    public boolean _isAssignable(IsAssignable ctx, DType that) {
        if (that instanceof UInt8Type) {
            return true;
        }
        return false;
    }

    @Override
    public TypeSystem typeSystem() {
        return ts;
    }
}
