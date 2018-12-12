package com.dexscript.type;

import org.jetbrains.annotations.NotNull;

public class Int64Type implements NamedType {

    @Override
    public String toString() {
        return name();
    }

    @Override
    public @NotNull String name() {
        return "int64";
    }

    @Override
    public String javaClassName() {
        return Long.class.getCanonicalName();
    }

    @Override
    public boolean _isSubType(TypeComparisonContext ctx, Type that) {
        if (that instanceof Int64Type) {
            return true;
        }
        if (that instanceof IntegerLiteralType) {
            try {
                Long.valueOf(((IntegerLiteralType) that).literalValue());
                return true;
            } catch (NumberFormatException ignored) {
                return false;
            }
        }
        return false;
    }

    @Override
    public String initValue() {
        return "0L";
    }
}
