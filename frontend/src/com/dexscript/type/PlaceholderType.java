package com.dexscript.type;

public final class PlaceholderType extends NamedType {

    private final Type constraint;

    public PlaceholderType(String name, Type constraint) {
        super(name, "Object");
        this.constraint = constraint;
    }

    @Override
    public boolean isAssignableFrom(Substituted substituted, Type that) {
        if (substituted.get(this) != null) {
            return substituted.get(this).equals(that);
        }
        if (constraint.isAssignableFrom(substituted, that)) {
            substituted.put(this, that);
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "<" + name() + ">: " + constraint;
    }
}
