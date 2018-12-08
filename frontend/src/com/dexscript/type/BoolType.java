package com.dexscript.type;

public class BoolType extends TopLevelType {

    public BoolType() {
        super("bool", "Boolean");
    }

    @Override
    public String toString() {
        return "bool";
    }

    @Override
    public boolean isAssignableFrom(Type that) {
        if (super.isAssignableFrom(that)) {
            return true;
        }
        if (that instanceof BoolType) {
            return true;
        }
        return false;
    }
}
