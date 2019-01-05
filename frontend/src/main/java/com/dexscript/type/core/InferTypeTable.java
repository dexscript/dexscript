package com.dexscript.type.core;

import com.dexscript.ast.DexInterface;
import com.dexscript.ast.core.DexElement;

import java.util.HashMap;
import java.util.Map;

public interface InferTypeTable<E extends DexElement> {

    Map<Class<? extends DexElement>, InferTypeTable> handlers = new HashMap<Class<? extends DexElement>, InferTypeTable>() {
    };

    static <T extends DexElement> void register(Class<T> clazz, InferTypeTable<T> inferTypeTable) {
        handlers.put(clazz, inferTypeTable);
    }

    TypeTable handle(TypeSystem ts, E elem);

    static TypeTable $(TypeSystem ts, DexElement elem) {
        TypeTable typeTable = elem.attachmentOfType(TypeTable.class);
        if (typeTable != null) {
            return typeTable;
        }
        InferTypeTable inferTypeTable = handlers.get(elem.getClass());
        if (inferTypeTable == null) {
            typeTable = new TypeTable(ts);
        } else {
            typeTable = inferTypeTable.handle(ts, elem);
        }
        elem.attach(typeTable);
        return typeTable;
    }
}
