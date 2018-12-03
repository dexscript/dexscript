package com.dexscript.type;

public class StringType extends TopLevelType {

    public StringType() {
        super("string", "String");
    }

    @Override
    public boolean isAssignableFrom(Type that) {
        if (super.isAssignableFrom(that)) {
            return true;
        }
        return that instanceof StringType || that instanceof StringLiteralType;
    }

    @Override
    public String toString() {
        return "string";
    }
}
