package com.dexscript.type;

public class AnyType extends Type {

    public AnyType() {
        super("Object");
    }

    @Override
    public boolean isAssignableFrom(Substituted substituted, Type that) {
        return true;
    }
}
