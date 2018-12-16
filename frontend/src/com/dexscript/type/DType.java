package com.dexscript.type;

import java.util.HashMap;

public interface DType {

    default boolean isAssignableFrom(DType that) {
        return isAssignableFrom(new TypeComparisonContext(new HashMap<>()), that);
    }

    default boolean isAssignableFrom(TypeComparisonContext ctx, DType that) {
        if (!ctx.isTopLevel()) {
            return _isAssignableFrom(ctx, that);
        }
        TypeComparison comparison = new TypeComparison(this, that);
        TypeComparisonCache comparisonCache = typeSystem().comparisonCache();
        Boolean assignableFrom = comparisonCache.get(comparison);
        if (assignableFrom != null) {
            return assignableFrom;
        }
        assignableFrom = _isAssignableFrom(ctx, that);
        comparisonCache.set(comparison, assignableFrom);
        return assignableFrom;
    }

    default boolean _isAssignableFrom(TypeComparisonContext ctx, DType that) {
        if (this.equals(that)) {
            if (ctx.shouldLog()) {
                ctx.log(true, this, that, "they are equal");
            }
            return true;
        }
        DType sub = ctx.getSubstituted(this);
        if (sub != null) {
            boolean assignable = sub.equals(that);
            // widen string literal to string
            if (that instanceof StringLiteralType) {
                assignable |= sub.isAssignableFrom(ctx, that);
            }
            if (ctx.shouldLog()) {
                ctx.log(assignable, this, that, this + " sub to " + sub);
            }
            return assignable;
        }
        if (that instanceof IntersectionType) {
            for (DType elem : ((IntersectionType) that).types()) {
                if (this.isAssignableFrom(ctx, elem)) {
                    return true;
                }
            }
            return false;
        }
        if (that instanceof UnionType) {
            for (DType elem : ((UnionType) that).types()) {
                if (!this.isAssignableFrom(ctx, elem)) {
                    return false;
                }
            }
            return true;
        }
        return _isSubType(ctx, that);
    }

    boolean _isSubType(TypeComparisonContext ctx, DType that);

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
