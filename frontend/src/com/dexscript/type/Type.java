package com.dexscript.type;

import java.util.HashMap;

public abstract class Type {

    private final String javaClassName;

    public Type(String javaClassName) {
        this.javaClassName = javaClassName;
    }

    public final String javaClassName() {
        return javaClassName;
    }

    public final boolean isAssignableFrom(Type that) {
        TypeComparisonContext ctx = new TypeComparisonContext(new HashMap<>());
        boolean result = isAssignableFrom(ctx, that);
        return result;
    }

    public final boolean isAssignableFrom(TypeComparisonContext ctx, Type that) {
        if (this.equals(that)) {
            if (ctx.shouldLog()) {
                ctx.log(true, this, that, "they are equal");
            }
            return true;
        }
        Type sub = ctx.getSubstituted(this);
        if (sub != null) {
            boolean assignable = sub.equals(that);
            if (ctx.shouldLog()) {
                ctx.log(assignable, this, that, this + " sub to " + sub);
            }
            return assignable;
        }
        if (that instanceof IntersectionType) {
            for (Type elem : ((IntersectionType) that).types()) {
                if (this.isAssignableFrom(ctx, elem)) {
                    return true;
                }
            }
            return false;
        }
        if (that instanceof UnionType) {
            for (Type elem : ((UnionType) that).types()) {
                if (!this.isAssignableFrom(ctx, elem)) {
                    return false;
                }
            }
            return true;
        }
        return isSubType(ctx, that);
    }

    protected abstract boolean isSubType(TypeComparisonContext ctx, Type that);

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
