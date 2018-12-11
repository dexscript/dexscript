package com.dexscript.type;

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
        Subs subs = new Subs();
        boolean result = isAssignableFrom(subs, that);
        return result;
    }

    public boolean isAssignableFrom(Subs subs, Type that) {
        if (this.equals(that)) {
            return true;
        }
        if (that instanceof SameType) {
            return this.equals(((SameType)that).sameType());
        }
        if (that instanceof IntersectionType) {
            for (Type elem : ((IntersectionType) that).types()) {
                if (this.isAssignableFrom(subs, elem)) {
                    return true;
                }
            }
            return false;
        }
        if (that instanceof UnionType) {
            for (Type elem : ((UnionType) that).types()) {
                if (!this.isAssignableFrom(subs, elem)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    protected Type expand(Map<Type, Type> lookup) {
        Type expanded = lookup.get(this);
        if (expanded != null) {
            return expanded;
        }
        return this;
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
