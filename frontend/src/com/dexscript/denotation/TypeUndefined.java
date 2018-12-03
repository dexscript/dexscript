package com.dexscript.denotation;

public class TypeUndefined extends Type {

    public TypeUndefined() {
        super(null);
    }

    @Override
    public boolean isAssignableFrom(Type that) {
        return false;
    }
}
