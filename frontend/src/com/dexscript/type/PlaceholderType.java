package com.dexscript.type;

import org.jetbrains.annotations.NotNull;

public final class PlaceholderType implements NamedType {

    private final String name;
    private final DType constraint;

    public PlaceholderType(String name, DType constraint) {
        this.name = name;
        this.constraint = constraint;
    }

    @Override
    public boolean _isSubType(TypeComparisonContext ctx, DType that) {
        if (constraint.isAssignableFrom(ctx, that)) {
            if (that instanceof StringLiteralType) {
                ctx.putSubstituted(this, BuiltinTypes.STRING);
                return true;
            }
            ctx.putSubstituted(this, that);
            return true;
        }
        return false;
    }

    @Override
    public String description() {
        return "<" + name() + ">: " + constraint;
    }

    @Override
    public String toString() {
        return name();
    }

    @Override
    public @NotNull String name() {
        return name;
    }
}
