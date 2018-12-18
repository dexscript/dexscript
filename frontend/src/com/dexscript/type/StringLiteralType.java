package com.dexscript.type;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class StringLiteralType implements DType {

    private final TypeSystem ts;
    @NotNull
    private final String literalValue;

    public StringLiteralType(TypeSystem ts, @NotNull String literalValue) {
        this.ts = ts;
        this.literalValue = literalValue;
    }

    @Override
    public boolean _isAssignable(IsAssignable ctx, DType that) {
        return that.equals(this);
    }

    @Override
    public TypeSystem typeSystem() {
        return ts;
    }

    @NotNull
    public String literalValue() {
        return literalValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StringLiteralType that = (StringLiteralType) o;
        return literalValue.equals(that.literalValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(literalValue);
    }

    @Override
    public String toString() {
        return "'" + literalValue + "'";
    }
}
