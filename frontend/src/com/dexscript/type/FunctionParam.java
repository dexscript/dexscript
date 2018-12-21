package com.dexscript.type;

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
}
