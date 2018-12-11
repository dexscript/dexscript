package com.dexscript.type;

public class UndefinedType extends NamedType {

    public UndefinedType() {
        super("undefined", null);
    }

    @Override
    public boolean isAssignableFrom(Subs subs, Type that) {
        return false;
    }

    @Override
    public String toString() {
        return "undefined";
    }
}
