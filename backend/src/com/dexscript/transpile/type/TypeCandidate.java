package com.dexscript.transpile.type;

import com.dexscript.type.DType;

public class TypeCandidate {

    private final String javaClassName;
    private final boolean isInterface;
    private final DType type;

    public TypeCandidate(String javaClassName, boolean isInterface, DType type) {
        this.javaClassName = javaClassName;
        this.isInterface = isInterface;
        this.type = type;
    }

    public String javaClassName() {
        return javaClassName;
    }

    public boolean isInterface() {
        return isInterface;
    }

    public DType type() {
        return type;
    }
}
