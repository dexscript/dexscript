package com.dexscript.type;

public class AnyType extends Type {

    public AnyType() {
        super("Object");
    }

    @Override
    protected boolean isSubType(TypeComparisonContext ctx, Type that) {
        return true;
    }

    @Override
    public String toString() {
        return "interface{}";
    }
}
