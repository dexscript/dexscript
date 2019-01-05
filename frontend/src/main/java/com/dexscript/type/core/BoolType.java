package com.dexscript.type.core;

import java.util.Objects;

public class BoolType implements DType {

    private final TypeSystem ts;

    public BoolType(TypeSystem ts) {
        this.ts = ts;
    }

    @Override
    public String toString() {
        return "bool";
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
