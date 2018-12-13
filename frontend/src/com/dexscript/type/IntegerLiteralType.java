package com.dexscript.type;

import java.util.Objects;

public class IntegerLiteralType implements Type {

    private final String literalValue;

    public IntegerLiteralType(String literalValue) {
        this.literalValue = literalValue;
    }

    @Override
    public boolean _isSubType(TypeComparisonContext ctx, Type that) {
        return that.equals(this);
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
