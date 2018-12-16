package com.dexscript.type;

import org.jetbrains.annotations.NotNull;

public class StringType implements NamedType {

    @Override
    public boolean _isSubType(TypeComparisonContext ctx, DType that) {
        return that instanceof StringType || that instanceof StringLiteralType;
    }

    @Override
    public String toString() {
        return name();
    }

    @Override
    public @NotNull String name() {
        return "string";
    }
}
