package com.dexscript.type.core;

import java.util.Objects;

class Float64Type implements DType {

    private final TypeSystem ts;

    Float64Type(TypeSystem ts) {
        this.ts = ts;
    }

    @Override
    public boolean _isAssignable(IsAssignable ctx, DType that) {
        return this.equals(that) || ts.isFloatConst(that) || ts.isIntegerConst(that);
    }

    @Override
    public TypeSystem typeSystem() {
        return ts;
    }

    @Override
    public String toString() {
        return "float64";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Float64Type that = (Float64Type) o;
        return Objects.equals(ts, that.ts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ts);
    }
}
