package com.dexscript.type;

import org.jetbrains.annotations.NotNull;

public class StringType implements NamedType {

    private final TypeSystem ts;

    public StringType(TypeSystem ts) {
        this.ts = ts;
        ts.defineType(this);
    }

    @Override
    public boolean _isAssignable(IsAssignable ctx, DType that) {
        return that instanceof StringType || that instanceof StringLiteralType || that instanceof StringConstType;
    }

    @Override
    public TypeSystem typeSystem() {
        return ts;
    }

    @Override
    public String toString() {
        return name();
    }

    @Override
    public @NotNull String name() {
        return "string";
    }
}
