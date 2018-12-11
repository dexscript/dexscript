package com.dexscript.type;

public class VoidType extends NamedType {

    public VoidType() {
        super("void", "Object");
    }

    @Override
    public boolean isAssignableFrom(Type that) {
        return that instanceof VoidType;
    }

    @Override
    public String toString() {
        return "void";
    }
}
