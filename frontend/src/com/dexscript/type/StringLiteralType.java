package com.dexscript.type;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class StringLiteralType extends Type {

    @NotNull
    private final String literalValue;

    public StringLiteralType(@NotNull String literalValue) {
        super("String");
        this.literalValue = literalValue;
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
