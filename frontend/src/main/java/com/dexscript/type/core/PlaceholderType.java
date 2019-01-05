package com.dexscript.type.core;

import java.util.Objects;

public final class PlaceholderType implements DType {

    private final TypeSystem ts;
    private final String name;
    private final DType constraint;

    public PlaceholderType(TypeSystem ts, String name, DType constraint) {
        this.ts = ts;
        this.name = name;
        this.constraint = constraint;
    }

    @Override
    public boolean _isAssignable(IsAssignable ctx, DType that) {
        if (that instanceof PlaceholderType) {
            that = ((PlaceholderType) that).constraint;
        }
        if (new IsAssignable(ctx, "constraint", constraint, that).result()) {
            ctx.substitute(this, that);
            return true;
        }
        return false;
    }

    @Override
    public TypeSystem typeSystem() {
        return ts;
    }

    @Override
    public String toString() {
        return name;
    }

    public String description() {
        return "<" + name + ">: " + constraint;
    }

    public String name() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlaceholderType that = (PlaceholderType) o;
        return Objects.equals(ts, that.ts) &&
                Objects.equals(name, that.name) &&
                Objects.equals(constraint, that.constraint);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ts, name, constraint);
    }
}
