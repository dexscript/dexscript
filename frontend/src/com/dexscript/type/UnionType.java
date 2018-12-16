package com.dexscript.type;

import java.util.ArrayList;
import java.util.List;

public class UnionType implements DType {

    private final TypeSystem ts;
    private final List<DType> types;

    public UnionType(TypeSystem ts, DType type1, DType type2) {
        this.ts = ts;
        types = new ArrayList<>();
        types.add(type1);
        types.add(type2);
    }

    public UnionType(TypeSystem ts, List<DType> types) {
        this.ts = ts;
        this.types = types;
    }

    public List<DType> types() {
        return types;
    }

    @Override
    public DType union(DType that) {
        ArrayList<DType> types = new ArrayList<>(this.types);
        types.add(that);
        return new UnionType(ts, types);
    }

    @Override
    public DType intersect(DType that) {
        if (that instanceof UnionType) {
            List<DType> thatTypes = ((UnionType) that).types;
            List<DType> union = new ArrayList<>();
            for (DType type : this.types) {
                if (thatTypes.contains(type)) {
                    union.add(type);
                }
            }
            return new UnionType(ts, union);
        }
        return new IntersectionType(ts, this, that);
    }

    @Override
    public TypeSystem typeSystem() {
        return ts;
    }

    @Override
    public boolean _isSubType(TypeComparisonContext ctx, DType that) {
        for (DType type : types) {
            if (type.isAssignableFrom(ctx, that)) {
                return true;
            }
        }
        return false;
    }
}
