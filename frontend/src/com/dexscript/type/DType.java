package com.dexscript.type;

import java.util.HashMap;

public interface DType {

    default boolean isAssignableFrom(DType that) {
        return isAssignableFrom(new TypeComparisonCache(), that);
    }

    default boolean isAssignableFrom(TypeComparisonCache cache, DType that) {
        HashMap<DType, DType> collector = new HashMap<>();
        TypeComparisonContext ctx = new TypeComparisonContext(cache, collector);
        boolean result = isAssignableFrom(ctx, that);
        return result;
    }

    default boolean isAssignableFrom(TypeComparisonContext ctx, DType that) {
        TypeComparison comparison = new TypeComparison(this, that);
        Boolean assignableFrom = ctx.cache().get(comparison);
        if (assignableFrom != null) {
            return assignableFrom;
        }
        assignableFrom = _isAssignableFrom(ctx, that);
        ctx.cache().set(comparison, assignableFrom);
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
                assignable |= sub.isAssignableFrom(ctx.cache(), that);
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
        return new UnionType(this, that);
    }

    default DType intersect(DType that) {
        return new IntersectionType(this, that);
    }

    default String initValue() {
        return null;
    }

    default String description() {
        return toString();
    }
}
