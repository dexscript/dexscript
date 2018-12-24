package com.dexscript.type;

import org.jetbrains.annotations.NotNull;

public class UndefinedType implements NamedType {

    private final TypeSystem ts;

    public UndefinedType(TypeSystem ts) {
        this.ts = ts;
    }

    @Override
    public boolean _isAssignable(IsAssignable ctx, DType that) {
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
