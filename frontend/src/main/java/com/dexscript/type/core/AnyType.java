package com.dexscript.type.core;

import com.dexscript.ast.core.DexElement;
import com.dexscript.ast.type.DexAmsInfType;

import java.util.HashMap;

public class AnyType implements DType {

    static {
        InferType.handlers.putAll(new HashMap<Class<? extends DexElement>, InferType>() {{
            put(DexAmsInfType.class, (ts, localTypeTable, elem) -> {
                DexAmsInfType infType = (DexAmsInfType) elem;
                if (infType.functions().isEmpty() && infType.methods().isEmpty()) {
                    return ts.ANY;
                }
                throw new UnsupportedOperationException("not implemented");
            });
        }});
    }

    private final TypeSystem ts;

    public AnyType(TypeSystem ts) {
        this.ts = ts;
    }

    @Override
    public boolean _isAssignable(IsAssignable ctx, DType that) {
        return true;
    }

    @Override
    public TypeSystem typeSystem() {
        return ts;
    }

    @Override
    public String toString() {
        return "interface{}";
    }
}
