package com.dexscript.type;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

class Float32Type implements NamedType {

    private final TypeSystem ts;

    Float32Type(TypeSystem ts) {
        this.ts = ts;
        ts.defineType(this);
    }

    @Override
    public @NotNull String name() {
        return "float32";
    }

    @Override
    public boolean _isAssignable(IsAssignable ctx, DType that) {
        return that instanceof Float32Type || that instanceof FloatConstType;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Float32Type that = (Float32Type) o;
        return Objects.equals(ts, that.ts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ts);
    }
}
