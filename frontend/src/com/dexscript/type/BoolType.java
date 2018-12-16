package com.dexscript.type;

import org.jetbrains.annotations.NotNull;

public class BoolType implements NamedType {

    private final TypeSystem ts;

    public BoolType(TypeSystem ts) {
        this.ts = ts;
        ts.defineType(this);
    }

    @Override
    public String toString() {
        return name();
    }

    @Override
    public boolean _isSubType(TypeComparisonContext ctx, DType that) {
        return that instanceof BoolType;
    }

    @Override
    public TypeSystem typeSystem() {
        return ts;
    }

    @Override
    public @NotNull String name() {
        return "bool";
    }
}
