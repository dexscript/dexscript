package com.dexscript.transpile.body;

import com.dexscript.transpile.skeleton.OutClass;
import com.dexscript.transpile.skeleton.OutField;

// attach to for loop
// set this flag to break out
public class OutBreakFlag {

    private final OutField field;

    public OutBreakFlag(OutClass oClass) {
        this.field = oClass.allocateField("breakFlag");
    }

    public String fieldName() {
        return field.value();
    }
}
