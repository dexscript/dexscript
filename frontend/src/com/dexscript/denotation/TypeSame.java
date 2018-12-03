package com.dexscript.denotation;

public class TypeSame extends Type {

    private final Type sameType;

    public TypeSame(Type sameType) {
        super(null);
        this.sameType = sameType;
    }

    @Override
    public boolean isAssignableFrom(Type that) {
        return sameType.equals(that);
    }
}
