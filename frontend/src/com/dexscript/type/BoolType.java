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
    public boolean isAssignableFrom(Substituted substituted, Type that) {
        if (super.isAssignableFrom(substituted, that)) {
            return true;
        }
        if (that instanceof BoolType) {
            return true;
        }
        return false;
    }
}
