package com.dexscript.type;

import java.util.Objects;

public class Int64Type implements DType {

    private final TypeSystem ts;

    public Int64Type(TypeSystem ts) {
        this.ts = ts;
    }

    @Override
    public String toString() {
        return "int64";
    }

    @Override
    public boolean _isAssignable(IsAssignable ctx, DType that) {
        if (that instanceof Int64Type) {
            return true;
        }
        if (that instanceof IntegerLiteralType) {
            return true;
        }
        if (that instanceof IntegerConstType) {
            try {
                Long.valueOf(((IntegerConstType) that).constValue());
                return true;
            } catch (NumberFormatException ignored) {
                return false;
            }
        }
        return false;
    }

    @Override
    public String initValue() {
        return "0L";
    }

    @Override
    public TypeSystem typeSystem() {
        return ts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Int64Type int64Type = (Int64Type) o;
        return Objects.equals(ts, int64Type.ts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ts);
    }
}
