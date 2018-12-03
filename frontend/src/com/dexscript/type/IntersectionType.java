package com.dexscript.type;

import java.util.ArrayList;
import java.util.List;

public class IntersectionType extends Type {

    private final List<Type> types;

    public IntersectionType(Type type1, Type type2) {
        super("Object");
        types = new ArrayList<>();
        types.add(type1);
        types.add(type2);
    }

    @Override
    public boolean isAssignableFrom(Type that) {
        for (Type type : types) {
            if (!type.isAssignableFrom(that)) {
                return false;
            }
        }
        return true;
    }

    public List<Type> types() {
        return types;
    }
}
