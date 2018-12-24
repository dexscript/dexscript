package com.dexscript.type;

import org.jetbrains.annotations.NotNull;

public class UInt8Type implements NamedType {

    private final TypeSystem ts;

    public UInt8Type(TypeSystem ts) {
        this.ts = ts;
    }

    @Override
    public String toString() {
        return name();
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

    @Override
    public @NotNull String name() {
        return "uint8";
    }
}
