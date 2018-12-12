package com.dexscript.type;

import org.jetbrains.annotations.NotNull;

public final class PlaceholderType implements NamedType {

    private final String name;
    private final Type constraint;

    public PlaceholderType(String name, Type constraint) {
        this.name = name;
        this.constraint = constraint;
    }

    @Override
    public String javaClassName() {
        return Object.class.getCanonicalName();
    }

    @Override
    public boolean _isSubType(TypeComparisonContext ctx, Type that) {
        if (constraint.isAssignableFrom(ctx, that)) {
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
