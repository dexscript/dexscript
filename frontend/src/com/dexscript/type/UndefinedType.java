package com.dexscript.type;

import org.jetbrains.annotations.NotNull;

public class UndefinedType implements NamedType {

    @Override
    public String javaClassName() {
        return null;
    }

    @Override
    public boolean _isSubType(TypeComparisonContext ctx, Type that) {
        return false;
    }

    @Override
    public String toString() {
        return "undefined";
    }

    @Override
    public @NotNull String name() {
        return "undefined";
    }
}
