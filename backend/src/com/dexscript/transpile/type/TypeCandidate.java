package com.dexscript.transpile.type;

import com.dexscript.type.Type;

public class TypeCandidate {

    private final String javaClassName;
    private final boolean isInterface;
    private final Type type;

    public TypeCandidate(String javaClassName, boolean isInterface, Type type) {
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

    public Type type() {
        return type;
    }
}
