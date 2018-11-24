package com.dexscript.transpile;

import com.dexscript.ast.core.DexElement;
import com.dexscript.resolve.Denotation;

public final class OutField {

    public final DexElement iElem;
    public final String fieldName;
    public final Denotation.Type fieldType;

    public OutField(DexElement iElem, String fieldName, Denotation.Type fieldType) {
        this.iElem = iElem;
        this.fieldName = fieldName;
        this.fieldType = fieldType;
    }
}
