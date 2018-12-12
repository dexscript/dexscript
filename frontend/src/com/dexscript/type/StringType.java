package com.dexscript.type;

public class StringType extends NamedType {

    public StringType() {
        super("string", "java.lang.String");
    }

    @Override
    public boolean isAssignableFrom(Substituted substituted, Type that) {
        if (super.isAssignableFrom(substituted, that)) {
            return true;
        }
        return that instanceof StringType || that instanceof StringLiteralType;
    }

    @Override
    public String toString() {
        return "string";
    }
}
