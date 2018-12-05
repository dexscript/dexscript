package com.dexscript.transpile;

import com.dexscript.type.Type;

public final class OutField extends OutValue {

    private final Type type;

    public OutField(String fieldName, Type type) {
        super(fieldName);
        this.type = type;
    }

    public Type type() {
        return type;
    }
}
