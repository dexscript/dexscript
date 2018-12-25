package com.dexscript.type;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class StringLiteralType implements DType {

    private final TypeSystem ts;
    @NotNull
    private final String val;

    public StringLiteralType(TypeSystem ts, @NotNull String val) {
        this.ts = ts;
        this.val = val;
    }

    @Override
    public boolean _isAssignable(IsAssignable ctx, DType that) {
        if (that instanceof StringConstType) {
            return val.equals(((StringConstType) that).constValue());
        }
        return that.equals(this);
    }

    @Override
    public TypeSystem typeSystem() {
        return ts;
    }

    @NotNull
    public String literalValue() {
        return val;
    }

    @Override
    public String toString() {
        return "'" + val + "'";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StringLiteralType that = (StringLiteralType) o;
        return Objects.equals(ts, that.ts) &&
                val.equals(that.val);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ts, val);
    }
}
