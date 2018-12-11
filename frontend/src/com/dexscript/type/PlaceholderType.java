package com.dexscript.type;

class PlaceholderType extends NamedType {

    private final Type constraint;

    public PlaceholderType(String name, Type constraint) {
        super(name, "Object");
        this.constraint = constraint;
    }

    @Override
    public boolean isAssignableFrom(Subs subs, Type that) {
        if (constraint.isAssignableFrom(subs, that)) {
            subs.addSub(this, that);
            return true;
        }
        return super.isAssignableFrom(subs, that);
    }

    @Override
    public String toString() {
        return "<" + name() + ">: " + constraint;
    }
}
