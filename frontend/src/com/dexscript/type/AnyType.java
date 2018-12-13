package com.dexscript.type;

public class AnyType implements Type {

    @Override
    public boolean _isSubType(TypeComparisonContext ctx, Type that) {
        return true;
    }

    @Override
    public String toString() {
        return "interface{}";
    }
}
