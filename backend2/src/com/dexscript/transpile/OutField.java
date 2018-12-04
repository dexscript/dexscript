package com.dexscript.transpile;

import com.dexscript.ast.core.DexElement;
import com.dexscript.type.Type;

public final class OutField extends OutValue {

    private final DexElement iElem;
    private final Type type;

    public OutField(DexElement iElem, String fieldName, Type type) {
        super(fieldName);
        this.iElem = iElem;
        this.type = type;
    }

    public DexElement iElem() {
        return iElem;
    }

    public Type type() {
        return type;
    }
}
