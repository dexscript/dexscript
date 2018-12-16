package com.dexscript.type;

import java.util.Objects;

public class IntegerLiteralType implements DType {

    private final TypeSystem ts;
    private final String literalValue;

    public IntegerLiteralType(TypeSystem ts, String literalValue) {
        this.ts = ts;
        this.literalValue = literalValue;
    }

    @Override
    public boolean _isSubType(TypeComparisonContext ctx, DType that) {
        return that.equals(this);
    }

    @Override
    public TypeSystem typeSystem() {
        return ts;
    }

    public String literalValue() {
        return literalValue;
    }

    @Override
    public String toString() {
        return literalValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IntegerLiteralType that = (IntegerLiteralType) o;
        return Objects.equals(literalValue, that.literalValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(literalValue);
    }
}
