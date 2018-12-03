package com.dexscript.denotation;

import org.jetbrains.annotations.NotNull;

public class TypeStringLiteral extends Type {

    private final String literalValue;

    public TypeStringLiteral(@NotNull String literalValue) {
        super("String");
        this.literalValue = literalValue;
    }

    public String literalValue() {
        return literalValue;
    }
}
