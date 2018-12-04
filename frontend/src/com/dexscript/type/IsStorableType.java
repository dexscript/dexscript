package com.dexscript.type;

public interface IsStorableType {
    static boolean $(Type type) {
        if (BuiltinTypes.UNDEFINED.equals(type)) {
            return false;
        }
        if (BuiltinTypes.VOID.equals(type)) {
            return false;
        }
        return true;
    }
}
