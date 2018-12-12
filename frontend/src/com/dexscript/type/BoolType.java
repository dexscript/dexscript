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
    protected boolean isSubType(TypeComparisonContext ctx, Type that) {
        return that instanceof BoolType;
    }
}
