package com.dexscript.type.core;

import com.dexscript.ast.core.DexElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface InferTypeTable<E extends DexElement> {

    Map<Class<? extends DexElement>, InferTypeTable> handlers = new HashMap<Class<? extends DexElement>, InferTypeTable>() {
    };

    static <T extends DexElement> void register(Class<T> clazz, InferTypeTable<T> inferTypeTable) {
        handlers.put(clazz, inferTypeTable);
    }

    TypeTable handle(TypeSystem ts, Map<DexElement, TypeTable> typeTableMap, E elem);

    static TypeTable $(TypeSystem ts, Map<DexElement, TypeTable> typeTableMap, DexElement elem) {
        if (typeTableMap == null) {
            TypeTable typeTable = elem.attachmentOfType(TypeTable.class);
            if (typeTable != null) {
                return typeTable;
            }
        }
        TypeTable typeTable;
        InferTypeTable inferTypeTable = handlers.get(elem.getClass());
        if (inferTypeTable == null) {
            typeTable = null;
        } else {
            typeTable = inferTypeTable.handle(ts, typeTableMap, elem);
        }
        if (typeTableMap == null && typeTable != null) {
            elem.attach(typeTable);
        }
        return typeTable;
    }

    static List<TypeTable> typeTablesOf(TypeSystem ts, Map<DexElement, TypeTable> typeTableMap, DexElement elem) {
        List<TypeTable> typeTables = new ArrayList<>();
        while (elem != null) {
            if (typeTableMap != null && typeTableMap.containsKey(elem)) {
                TypeTable typeTable = typeTableMap.get(elem);
                if (typeTable != null) {
                    typeTables.add(typeTable);
                }
                elem = elem.parent();
                continue;
            }
            TypeTable typeTable = InferTypeTable.$(ts, typeTableMap, elem);
            if (typeTable != null) {
                typeTables.add(typeTable);
            }
            elem = elem.parent();
        }
        typeTables.add(ts.typeTable());
        return typeTables;
    }
}
