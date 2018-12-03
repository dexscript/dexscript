package com.dexscript.denotation;

public abstract class Type {

    private final String javaClassName;

    public Type(String javaClassName) {
        this.javaClassName = javaClassName;
    }

    public final String javaClassName() {
        return javaClassName;
    }

    public boolean isAssignableFrom(Type that) {
        return this.equals(that);
    }
}
