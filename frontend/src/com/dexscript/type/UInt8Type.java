package com.dexscript.type;

public class UInt8Type extends NamedType {

    public UInt8Type() {
        super("uint8", "com.dexscript.runtime.UInt8");
    }

    @Override
    public String toString() {
        return "uint8";
    }

    @Override
    public boolean isAssignableFrom(Substituted substituted, Type that) {
        if (super.isAssignableFrom(substituted, that)) {
            return true;
        }
        if (that instanceof UInt8Type) {
            return true;
        }
        if (that instanceof IntegerLiteralType) {
            throw new UnsupportedOperationException("not implemented: test data range");
        }
        return false;
    }
}
