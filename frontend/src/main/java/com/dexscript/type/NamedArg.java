package com.dexscript.type;

import java.util.Objects;

public class NamedArg {

    private final String name;
    private final DType type;

    public NamedArg(String name, DType type) {
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
        NamedArg namedArg = (NamedArg) o;
        return Objects.equals(name, namedArg.name) &&
                Objects.equals(type, namedArg.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type);
    }
}

