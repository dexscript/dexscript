package com.dexscript.type;

public interface IsStorableType {
    static boolean $(DType type) {
        if (type instanceof UndefinedType) {
            return false;
        }
        if (type instanceof VoidType) {
            return false;
        }
        return true;
    }
}
