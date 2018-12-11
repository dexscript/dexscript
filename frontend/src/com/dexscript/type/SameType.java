package com.dexscript.type;

public final class SameType extends Type {

    private final Type sameType;

    public SameType(Type sameType) {
        super(null);
        this.sameType = sameType;
    }

    @Override
    public boolean isAssignableFrom(Subs subs, Type that) {
        return sameType.equals(that);
    }

    @Override
    public String toString() {
        return "SameType{" +
                "sameType=" + sameType +
                '}';
    }

    public Type sameType() {
        return sameType;
    }
}
