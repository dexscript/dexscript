package com.dexscript.type;

import org.jetbrains.annotations.NotNull;

public class VoidType implements NamedType {

    private final TypeSystem ts;

    public VoidType(TypeSystem ts) {
        this.ts = ts;
        ts.defineType(this);
    }

    @Override
    public boolean _isSubType(TypeComparisonContext ctx, DType that) {
        return that instanceof VoidType;
    }

    @Override
    public TypeSystem typeSystem() {
        return ts;
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
