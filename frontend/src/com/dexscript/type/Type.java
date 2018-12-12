package com.dexscript.type;

import java.util.HashMap;
import java.util.Map;

public abstract class Type {

    private final String javaClassName;

    public Type(String javaClassName) {
        this.javaClassName = javaClassName;
    }

    public final String javaClassName() {
        return javaClassName;
    }

    public boolean isAssignableFrom(Type that) {
        Substituted substituted = new Substituted(new HashMap<>());
        boolean result = isAssignableFrom(substituted, that);
        return result;
    }

    public boolean isAssignableFrom(Substituted substituted, Type that) {
//        if (this.equals(that)) {
//            return true;
//        }
        if (that instanceof IntersectionType) {
            for (Type elem : ((IntersectionType) that).types()) {
                if (this.isAssignableFrom(substituted, elem)) {
                    return true;
                }
            }
            return false;
        }
        if (that instanceof UnionType) {
            for (Type elem : ((UnionType) that).types()) {
                if (!this.isAssignableFrom(substituted, elem)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public Type union(Type that) {
        return new UnionType(this, that);
    }

    public Type intersect(Type that) {
        return new IntersectionType(this, that);
    }

    public String initValue() {
        return null;
    }
}
