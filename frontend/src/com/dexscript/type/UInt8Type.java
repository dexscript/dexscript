package com.dexscript.type;

import org.jetbrains.annotations.NotNull;

public class UInt8Type implements NamedType {

    @Override
    public String toString() {
        return name();
    }

    @Override
    public String javaClassName() {
        return "com.dexscript.runtime.UInt8";
    }

    @Override
    public boolean _isSubType(TypeComparisonContext ctx, Type that) {
        if (that instanceof UInt8Type) {
            return true;
        }
        return false;
    }

    @Override
    public @NotNull String name() {
        return "uint8";
    }
}
