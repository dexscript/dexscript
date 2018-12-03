package com.dexscript.type;

public class SameType extends Type {

    private final Type sameType;

    public SameType(Type sameType) {
        super(null);
        this.sameType = sameType;
    }

    @Override
    public boolean isAssignableFrom(Type that) {
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
