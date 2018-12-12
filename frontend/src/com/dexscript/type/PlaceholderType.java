package com.dexscript.type;

public final class PlaceholderType extends NamedType {

    private final Type constraint;

    public PlaceholderType(String name, Type constraint) {
        super(name, "Object");
        this.constraint = constraint;
    }

    @Override
    protected boolean isSubType(TypeComparisonContext ctx, Type that) {
        if (constraint.isAssignableFrom(ctx, that)) {
            ctx.putSubstituted(this, that);
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "<" + name() + ">: " + constraint;
    }
}
