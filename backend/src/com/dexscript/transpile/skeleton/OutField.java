package com.dexscript.transpile.skeleton;

import com.dexscript.transpile.body.OutValue;
import com.dexscript.type.DType;

public final class OutField extends OutValue {

    private final DType type;

    public OutField(String fieldName, DType type) {
        super(fieldName);
        this.type = type;
    }

    public DType type() {
        return type;
    }
}
