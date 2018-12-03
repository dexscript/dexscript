package com.dexscript.transpile;

import com.dexscript.ast.core.DexElement;
import com.dexscript.type.Type;

public final class OutField {

    public final DexElement iElem;
    public final String fieldName;
    public final Type fieldType;

    public OutField(DexElement iElem, String fieldName, Type fieldType) {
        this.iElem = iElem;
        this.fieldName = fieldName;
        this.fieldType = fieldType;
    }
}
