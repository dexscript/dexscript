package com.dexscript.type;

public class IntegerLiteralType extends Type {

    private final String literalValue;

    public IntegerLiteralType(String literalValue) {
        super("Object");
        this.literalValue = literalValue;
    }

    public String literalValue() {
        return literalValue;
    }

    @Override
    public String toString() {
        return literalValue;
    }
}
