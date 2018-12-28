package com.dexscript.transpile.skeleton;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public final class OutFields implements Iterable<OutField> {

    private Map<String, OutField> fields = new HashMap<>();

    public OutField allocate(String suggestedName) {
        suggestedName = "f" + suggestedName;
        OutField oField = tryAllocate(suggestedName);
        if (oField != null) {
            return oField;
        }
        int i = 2;
        while (true) {
            String newName = suggestedName + i;
            oField = tryAllocate(newName);
            if (oField != null) {
                return oField;
            }
            i += 1;
        }
    }

    private OutField tryAllocate(String fieldName) {
        if (fields.containsKey(fieldName)) {
            return null;
        }
        OutField field = new OutField(fieldName);
        fields.put(fieldName, field);
        return field;
    }

    @Override
    public Iterator<OutField> iterator() {
        return fields.values().iterator();
    }
}
