package com.dexscript.type;

public class AnyType implements DType {

    @Override
    public boolean _isSubType(TypeComparisonContext ctx, DType that) {
        return true;
    }

    @Override
    public String toString() {
        return "interface{}";
    }
}
