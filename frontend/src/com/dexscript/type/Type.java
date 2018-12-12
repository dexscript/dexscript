package com.dexscript.type;

import java.util.HashMap;

public interface Type {

    String javaClassName();

    default boolean isAssignableFrom(Type that) {
        HashMap<Type, Type> collector = new HashMap<>();
        TypeComparisonContext ctx = new TypeComparisonContext(collector);
        boolean result = isAssignableFrom(ctx, that);
        return result;
    }

    default boolean isAssignableFrom(TypeComparisonContext ctx, Type that) {
        if (this.equals(that)) {
            if (ctx.shouldLog()) {
                ctx.log(true, this, that, "they are equal");
            }
            return true;
        }
        Type sub = ctx.getSubstituted(this);
        if (sub != null && sub.equals(that)) {
            if (ctx.shouldLog()) {
                ctx.log(true, this, that, this + " sub to " + sub);
            }
            return true;
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
        return _isSubType(ctx, that);
    }

    boolean _isSubType(TypeComparisonContext ctx, Type that);

    default Type union(Type that) {
        return new UnionType(this, that);
    }

    default Type intersect(Type that) {
        return new IntersectionType(this, that);
    }

    default String initValue() {
        return null;
    }

    default String description() {
        return toString();
    }
}
