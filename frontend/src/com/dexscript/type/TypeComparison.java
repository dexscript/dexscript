package com.dexscript.type;

import java.util.Objects;

public class TypeComparison {

    private final DType to;
    private final DType from;

    public TypeComparison(DType to, DType from) {
        this.to = to;
        this.from = from;
    }

    public DType to() {
        return to;
    }

    public DType from() {
        return from;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TypeComparison that = (TypeComparison) o;
        return Objects.equals(to, that.to) &&
                Objects.equals(from, that.from);
    }

    @Override
    public int hashCode() {
        return Objects.hash(to, from);
    }

    @Override
    public String toString() {
        return to + " | " + from;
    }
}
