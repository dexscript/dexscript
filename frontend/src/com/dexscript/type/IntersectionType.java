package com.dexscript.type;

import java.util.ArrayList;
import java.util.List;

public class IntersectionType implements Type {

    private final List<Type> types;

    public IntersectionType(Type type1, Type type2) {
        types = new ArrayList<>();
        types.add(type1);
        types.add(type2);
    }

    @Override
    public boolean _isSubType(TypeComparisonContext ctx, Type that) {
        for (Type type : types) {
            if (!type.isAssignableFrom(ctx, that)) {
                return false;
            }
        }
        return true;
    }

    public List<Type> types() {
        return types;
    }
}
