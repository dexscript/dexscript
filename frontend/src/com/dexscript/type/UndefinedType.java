package com.dexscript.type;

import org.jetbrains.annotations.NotNull;

public class UndefinedType implements NamedType {

    private final TypeSystem ts;

    public UndefinedType(TypeSystem ts) {
        this.ts = ts;
        ts.defineType(this);
    }

    @Override
    public boolean _isSubType(TypeComparisonContext ctx, DType that) {
        return false;
    }

    @Override
    public TypeSystem typeSystem() {
        return ts;
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
