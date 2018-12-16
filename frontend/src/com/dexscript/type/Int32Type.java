package com.dexscript.type;

import org.jetbrains.annotations.NotNull;

public class Int32Type implements NamedType {

    private final TypeSystem ts;

    public Int32Type(TypeSystem ts) {
        this.ts = ts;
        ts.defineType(this);
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
    public boolean _isSubType(TypeComparisonContext ctx, DType that) {
        if (that instanceof Int32Type) {
            return true;
        }
        if (that instanceof IntegerLiteralType) {
            try {
                Integer.valueOf(((IntegerLiteralType) that).literalValue());
                return true;
            } catch (NumberFormatException ignored) {
                return false;
            }
        }
        return false;
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
