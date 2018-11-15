package com.dexscript.transpiler;

import com.dexscript.psi.DexType;

public class TransType {

    public static RuntimeType translateType(DexType type) {
        switch (type.getNode().getText()) {
            case "string":
                return new RuntimeType(RuntimeTypeKind.CONCRETE_OBJECT, "String");
            case "int64":
                return new RuntimeType(RuntimeTypeKind.CONCRETE_OBJECT, "Long");
            case "uint64":
                throw new UnsupportedOperationException("not implemented");
            case "int32":
                return new RuntimeType(RuntimeTypeKind.CONCRETE_OBJECT, "Integer");
            default:
                return new RuntimeType(RuntimeTypeKind.GENERIC_OBJECT, type.getNode().getText());
        }
    }
}
