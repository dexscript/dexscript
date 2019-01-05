package com.dexscript.type.core;

import java.util.Objects;

public class FunctionParam {

    private final String name;
    private final DType type;

    public FunctionParam(String name, DType type) {
        this.name = name;
        this.type = type;
    }

    public String name() {
        return name;
    }

    public DType type() {
        return type;
    }

    @Override
    public String toString() {
        return name + ": " + type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FunctionParam that = (FunctionParam) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type);
    }
}
