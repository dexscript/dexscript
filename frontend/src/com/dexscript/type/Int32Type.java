package com.dexscript.type;

import org.jetbrains.annotations.NotNull;

public class Int32Type implements NamedType {

    private final TypeSystem ts;

    public Int32Type(TypeSystem ts) {
        this.ts = ts;
        ts.defineType(this);
    }

    @Override
    public boolean _isAssignable(IsAssignable ctx, DType that) {
        if (that instanceof IntegerConstType) {
            try {
                Integer.valueOf(((IntegerConstType) that).constValue());
                return true;
            } catch (NumberFormatException ignored) {
                return false;
            }
        }
        return that instanceof Int32Type;
    }

    @Override
    public String toString() {
        return name();
    }

    @Override
    public @NotNull String name() {
        return "int32";
    }

    @Override
    public String initValue() {
        return "0";
    }

    @Override
    public TypeSystem typeSystem() {
        return ts;
    }
}
