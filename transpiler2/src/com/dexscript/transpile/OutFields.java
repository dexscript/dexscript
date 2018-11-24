package com.dexscript.transpile;

import com.dexscript.ast.core.DexElement;
import com.dexscript.resolve.Denotation;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public final class OutFields implements Iterable<OutField> {

    private static Map<String, OutField> fields = new HashMap<>();

    public OutField allocate(DexElement iElem, Denotation.Type type) {
        String suggestedName = iElem.toString();
        OutField field = tryAllocate(suggestedName, iElem, type);
        if (field != null) {
            return field;
        }
        int i = 2;
        while(true) {
            String newName = suggestedName + i;
            field = tryAllocate(newName, iElem, type);
            if (field != null) {
                return field;
            }
            i += 1;
        }
    }

    private OutField tryAllocate(String name, DexElement iElem, Denotation.Type type) {
        if (!fields.containsKey(name)) {
            OutField field = new OutField(iElem, name, type);
            fields.put(name, field);
            return field;
        }
        return null;
    }

    @Override
    public Iterator<OutField> iterator() {
        return fields.values().iterator();
    }
}
