package com.dexscript.type;

import org.jetbrains.annotations.NotNull;

public class VoidType implements NamedType {

    @Override
    public boolean _isSubType(TypeComparisonContext ctx, DType that) {
        return that instanceof VoidType;
    }

    @Override
    public String toString() {
        return name();
    }

    @Override
    public @NotNull String name() {
        return "void";
    }
}
