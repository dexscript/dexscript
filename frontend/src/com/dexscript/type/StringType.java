package com.dexscript.type;

public class StringType extends NamedType {

    public StringType() {
        super("string", "java.lang.String");
    }

    @Override
    protected boolean isSubType(TypeComparisonContext ctx, Type that) {
        return that instanceof StringType || that instanceof StringLiteralType;
    }

    @Override
    public String toString() {
        return "string";
    }
}
