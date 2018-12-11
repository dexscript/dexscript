package com.dexscript.type;

public class BoolType extends NamedType {

    public BoolType() {
        super("bool", "java.lang.Boolean");
    }

    @Override
    public String toString() {
        return "bool";
    }

    @Override
    public boolean isAssignableFrom(Subs subs, Type that) {
        if (super.isAssignableFrom(subs, that)) {
            return true;
        }
        if (that instanceof BoolType) {
            return true;
        }
        return false;
    }
}
