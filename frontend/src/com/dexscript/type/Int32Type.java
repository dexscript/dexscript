package com.dexscript.type;

import java.util.Objects;

public class Int32Type implements DType {

    private final TypeSystem ts;

    public Int32Type(TypeSystem ts) {
        this.ts = ts;
    }

    @Override
    public boolean _isAssignable(IsAssignable ctx, DType that) {
        if (that instanceof IntegerConstType) {
            try {
                Integer.valueOf(((IntegerConstType) that).constValue());
                return true;
            } catch (NumberFormatException ignored) {
                return false;
            }
        }
        return that instanceof Int32Type;
    }

    @Override
    public String toString() {
        return "int32";
    }

    @Override
    public String initValue() {
        return "0";
    }

    @Override
    public TypeSystem typeSystem() {
        return ts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Int32Type int32Type = (Int32Type) o;
        return Objects.equals(ts, int32Type.ts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ts);
    }
}
