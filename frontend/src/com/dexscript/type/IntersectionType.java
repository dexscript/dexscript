package com.dexscript.type;

import java.util.ArrayList;
import java.util.List;

public class IntersectionType implements DType {

    private final List<DType> types;

    public IntersectionType(DType type1, DType type2) {
        types = new ArrayList<>();
        types.add(type1);
        types.add(type2);
    }

    @Override
    public boolean _isSubType(TypeComparisonContext ctx, DType that) {
        for (DType type : types) {
            if (!type.isAssignableFrom(ctx, that)) {
                return false;
            }
        }
        return true;
    }

    public List<DType> types() {
        return types;
    }
}
