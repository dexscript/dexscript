package com.dexscript.denotation;

public class TypeVoid extends TopLevelType {

    public TypeVoid() {
        super("void", "Object");
    }

    @Override
    public boolean isAssignableFrom(Type that) {
        return that instanceof TypeVoid;
    }
}
