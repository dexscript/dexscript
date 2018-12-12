package com.dexscript.type;

public class UndefinedType extends NamedType {

    public UndefinedType() {
        super("undefined", null);
    }

    @Override
    protected boolean isSubType(TypeComparisonContext ctx, Type that) {
        return false;
    }

    @Override
    public String toString() {
        return "undefined";
    }
}
