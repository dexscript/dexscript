package com.dexscript.type;

public class AnyType extends Type {

    public AnyType() {
        super("Object");
    }

    @Override
    public boolean isAssignableFrom(Subs subs, Type that) {
        return true;
    }
}
