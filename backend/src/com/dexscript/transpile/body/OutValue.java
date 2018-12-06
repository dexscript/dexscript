package com.dexscript.transpile.body;

import com.dexscript.ast.core.DexElement;

public class OutValue {

    private final String value;

    public OutValue(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    public static String of(DexElement elem) {
        OutValue outValue = elem.attachmentOfType(OutValue.class);
        if (outValue == null) {
            throw new IllegalStateException("missing out value: " + elem);
        }
        return outValue.value();
    }
}
