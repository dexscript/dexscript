package com.dexscript.type;

import org.jetbrains.annotations.NotNull;

public class StringLiteralType extends Type {

    private final String literalValue;

    public StringLiteralType(@NotNull String literalValue) {
        super("String");
        this.literalValue = literalValue;
    }

    public String literalValue() {
        return literalValue;
    }
}
