package com.dexscript.type;

import org.jetbrains.annotations.NotNull;

class Float64Type implements NamedType {

    private final TypeSystem ts;

    Float64Type(TypeSystem ts) {
        this.ts = ts;
        ts.defineType(this);
    }

    @Override
    public @NotNull String name() {
        return "float64";
    }

    @Override
    public boolean _isAssignable(IsAssignable ctx, DType that) {
        return that instanceof Float64Type || that instanceof FloatConstType;
    }

    @Override
    public TypeSystem typeSystem() {
        return ts;
    }

    @Override
    public String toString() {
        return name();
    }
}
