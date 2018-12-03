package com.dexscript.denotation;

import java.util.Map;

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

    protected Type expand(Map<Type, Type> lookup) {
        Type expanded = lookup.get(this);
        if (expanded != null) {
            return expanded;
        }
        return this;
    }
}
