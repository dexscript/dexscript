package com.dexscript.type;

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
}

