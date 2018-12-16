package com.dexscript.type;

import org.jetbrains.annotations.NotNull;

public class BoolType implements NamedType {

    @Override
    public String toString() {
        return name();
    }

    @Override
    public boolean _isSubType(TypeComparisonContext ctx, DType that) {
        return that instanceof BoolType;
    }

    @Override
    public @NotNull String name() {
        return "bool";
    }
}
