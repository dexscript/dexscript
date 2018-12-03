package com.dexscript.denotation;

public class TypeString extends TopLevelType {

    public TypeString() {
        super("string", "String");
    }

    @Override
    public boolean isAssignableFrom(Type that) {
        return that instanceof TypeString || that instanceof TypeStringLiteral;
    }

    @Override
    public String toString() {
        return "string";
    }
}
