package com.dexscript.type;

import org.jetbrains.annotations.NotNull;

public class Int64Type implements NamedType {

    private final TypeSystem ts;

    public Int64Type(TypeSystem ts) {
        this.ts = ts;
        ts.defineType(this);
    }

    @Override
    public String toString() {
        return name();
    }

    @Override
    public @NotNull String name() {
        return "int64";
    }

    @Override
    public boolean _isAssignable(IsAssignable ctx, DType that) {
        if (that instanceof Int64Type) {
            return true;
        }
        if (that instanceof IntegerLiteralType) {
            return true;
        }
        if (that instanceof IntegerConstType) {
            try {
                Long.valueOf(((IntegerConstType) that).constValue());
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

    @Override
    public TypeSystem typeSystem() {
        return ts;
    }
}
