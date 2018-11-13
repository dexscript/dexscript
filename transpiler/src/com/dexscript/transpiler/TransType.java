package com.dexscript.transpiler;

import com.dexscript.psi.DexType;

public class TransType {

    public static RuntimeType translateType(DexType type) {
        switch (type.getNode().getText()) {
            case "string":
                return new RuntimeType(RuntimeTypeKind.CONCRETE_OBJECT, "String");
            default:
                return new RuntimeType(RuntimeTypeKind.GENERIC_OBJECT, type.getNode().getText());
        }
    }
}
