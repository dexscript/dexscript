package com.dexscript.type;

public class VoidType extends NamedType {

    public VoidType() {
        super("void", void.class.getCanonicalName());
    }

    @Override
    protected boolean isSubType(TypeComparisonContext ctx, Type that) {
        return that instanceof VoidType;
    }

    @Override
    public String toString() {
        return "void";
    }
}
