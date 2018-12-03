package com.dexscript.type;

public class UndefinedType extends TopLevelType {

    public UndefinedType() {
        super("undefined", null);
    }

    @Override
    public boolean isAssignableFrom(Type that) {
        return false;
    }

    @Override
    public String toString() {
        return "undefined";
    }
}
