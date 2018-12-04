package com.dexscript.transpile;

import com.dexscript.ast.core.DexElement;
import com.dexscript.type.Type;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public final class OutFields implements Iterable<OutField> {

    private static Map<String, OutField> fields = new HashMap<>();

    public OutField allocate(DexElement iElem, Type type) {
        if (iElem.attachmentOfType(OutField.class) != null) {
            throw new IllegalStateException("has already allocated field for " + iElem);
        }
        String suggestedName = iElem.toString();
        OutField oField = tryAllocate(iElem, suggestedName, type);
        if (oField != null) {
            iElem.attach(oField);
            return oField;
        }
        int i = 2;
        while(true) {
            String newName = suggestedName + i;
            oField = tryAllocate(iElem, newName, type);
            if (oField != null) {
                iElem.attach(oField);
                return oField;
            }
            i += 1;
        }
    }

    private OutField tryAllocate(DexElement iElem, String fieldName, Type type) {
        if (fields.containsKey(fieldName)) {
            return null;
        }
        OutField field = new OutField(iElem, fieldName, type);
        fields.put(fieldName, field);
        return field;
    }

    @Override
    public Iterator<OutField> iterator() {
        return fields.values().iterator();
    }
}
