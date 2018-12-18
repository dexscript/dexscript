package com.dexscript.type;

public interface DType {

    boolean _isAssignable(IsAssignable ctx, DType that);

    default DType union(DType that) {
        return new UnionType(typeSystem(), this, that);
    }

    default DType intersect(DType that) {
        return new IntersectionType(typeSystem(), this, that);
    }

    default String initValue() {
        return null;
    }

    TypeSystem typeSystem();
}
