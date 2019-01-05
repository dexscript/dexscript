package com.dexscript.type.core;

import com.dexscript.ast.core.DexElement;
import com.dexscript.ast.type.DexVoidType;

import java.util.HashMap;

public class VoidType implements DType {

    static {
        InferType.handlers.putAll(new HashMap<Class<? extends DexElement>, InferType>() {{
            put(DexVoidType.class, (ts, localTypeTable, elemT) -> ts.VOID);
        }});
    }

    private final TypeSystem ts;

    public VoidType(TypeSystem ts) {
        this.ts = ts;
    }

    @Override
    public boolean _isAssignable(IsAssignable ctx, DType that) {
        return that instanceof VoidType;
    }

    @Override
    public TypeSystem typeSystem() {
        return ts;
    }

    @Override
    public String toString() {
        return "void";
    }
}
