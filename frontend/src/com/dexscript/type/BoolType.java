package com.dexscript.type;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class BoolType implements NamedType {

    private final TypeSystem ts;

    public BoolType(TypeSystem ts) {
        this.ts = ts;
    }

    @Override
    public String toString() {
        return name();
    }

    @Override
    public boolean _isAssignable(IsAssignable ctx, DType that) {
        return that instanceof BoolType || that instanceof BoolLiteralType || that instanceof BoolConstType;
    }

    @Override
    public TypeSystem typeSystem() {
        return ts;
    }

    @Override
    public @NotNull String name() {
        return "bool";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BoolType boolType = (BoolType) o;
        return Objects.equals(ts, boolType.ts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ts);
    }
}
