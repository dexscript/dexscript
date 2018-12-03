package com.dexscript.denotation;

public class TypeUndefined extends TopLevelType {

    public TypeUndefined() {
        super("undefined", null);
    }

    @Override
    public boolean isAssignableFrom(Type that) {
        return false;
    }
}
