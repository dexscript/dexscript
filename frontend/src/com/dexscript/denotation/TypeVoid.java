package com.dexscript.denotation;

public class TypeVoid extends Type {

    public TypeVoid() {
        super("Object");
    }

    @Override
    public boolean isAssignableFrom(Type that) {
        return that instanceof TypeVoid;
    }
}
