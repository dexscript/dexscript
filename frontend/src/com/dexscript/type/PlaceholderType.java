package com.dexscript.type;

public final class PlaceholderType implements DType {

    private final TypeSystem ts;
    private final String name;
    private final DType constraint;

    public PlaceholderType(TypeSystem ts, String name, DType constraint) {
        this.ts = ts;
        this.name = name;
        this.constraint = constraint;
    }

    @Override
    public boolean _isAssignable(IsAssignable ctx, DType that) {
        if (that instanceof PlaceholderType) {
            that = ((PlaceholderType) that).constraint;
        }
        if (new IsAssignable(ctx, "constraint", constraint, that).result()) {
            ctx.substitute(this, that);
            return true;
        }
        return false;
    }

    @Override
    public TypeSystem typeSystem() {
        return ts;
    }

    @Override
    public String toString() {
        return name;
    }

    public String description() {
        return "<" + name + ">: " + constraint;
    }
}
