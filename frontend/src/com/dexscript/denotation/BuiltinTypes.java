package com.dexscript.denotation;

import com.dexscript.ast.expr.DexReference;

public class BuiltinTypes implements TypeInterface.Resolve {

    public static final Type STRING = new TypeString();
    public static final Type VOID = new TypeVoid();
    public static final Type UNDEFINED = new TypeUndefined();

    @Override
    public Type resolveType(DexReference ref) {
        String typeName = ref.toString();
        switch (typeName) {
            case "string":
                return STRING;
            case "void":
                return VOID;
        }
        return UNDEFINED;
    }
}
