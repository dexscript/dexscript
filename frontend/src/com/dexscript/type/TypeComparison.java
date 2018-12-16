package com.dexscript.type;

import java.util.Objects;

public class TypeComparison {

    private final DType assignedTo;
    private final DType assignedFrom;

    public TypeComparison(DType assignedTo, DType assignedFrom) {
        this.assignedTo = assignedTo;
        this.assignedFrom = assignedFrom;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TypeComparison that = (TypeComparison) o;
        return Objects.equals(assignedTo, that.assignedTo) &&
                Objects.equals(assignedFrom, that.assignedFrom);
    }

    @Override
    public int hashCode() {
        return Objects.hash(assignedTo, assignedFrom);
    }
}
