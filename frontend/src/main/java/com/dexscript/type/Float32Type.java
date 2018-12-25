package com.dexscript.type;

import java.util.Objects;

class Float32Type implements DType {

    private final TypeSystem ts;

    Float32Type(TypeSystem ts) {
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
        return "float32";
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
