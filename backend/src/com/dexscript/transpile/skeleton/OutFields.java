package com.dexscript.transpile.skeleton;

import com.dexscript.type.IsStorableType;
import com.dexscript.type.Type;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public final class OutFields implements Iterable<OutField> {

    private Map<String, OutField> fields = new HashMap<>();

    public OutField allocate(String suggestedName, Type type) {
        suggestedName = "f" + suggestedName;
        if (!IsStorableType.$(type)) {
            throw new IllegalArgumentException("type is not storable: " + type);
        }
        OutField oField = tryAllocate(suggestedName, type);
        if (oField != null) {
            return oField;
        }
        int i = 2;
        while (true) {
            String newName = suggestedName + i;
            oField = tryAllocate(newName, type);
            if (oField != null) {
                return oField;
            }
            i += 1;
        }
    }

    private OutField tryAllocate(String fieldName, Type type) {
        if (fields.containsKey(fieldName)) {
            return null;
        }
        OutField field = new OutField(fieldName, type);
        fields.put(fieldName, field);
        return field;
    }

    @Override
    public Iterator<OutField> iterator() {
        return fields.values().iterator();
    }
}
