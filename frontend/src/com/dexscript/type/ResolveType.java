package com.dexscript.type;

import com.dexscript.ast.core.DexElement;
import com.dexscript.ast.type.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public interface ResolveType<E extends DexType> {

    interface OnUnknownElem {
        void handle(DexType elem);
    }

    class Events {
        public static OnUnknownElem ON_UNKNOWN_ELEM = elem -> {
            throw new UnsupportedOperationException("not implemented: " + elem.getClass());
        };
    }

    Map<Class<? extends DexElement>, ResolveType> handlers = new HashMap<Class<? extends DexElement>, ResolveType>() {{
        put(DexTypeRef.class, (typeTable, elem) -> typeTable.resolveType(elem.toString()));
        put(DexStringLiteralType.class, (typeTable, elem) -> {
            String literalValue = ((DexStringLiteralType) (elem)).literalValue();
            return new StringLiteralType(literalValue);
        });
        put(DexVoidType.class, (typeTable, elem) -> BuiltinTypes.VOID);
        put(DexArrayType.class, (typeTable, elem) -> {
            Type arrayElem = ResolveType.$(typeTable, ((DexArrayType) elem).left());
            return typeTable.resolveType("Array", Arrays.asList(arrayElem));
        });
    }};

    Type handle(TopLevelTypeTable typeTable, E elem);

    static Type $(TopLevelTypeTable typeTable, DexType elem) {
        ResolveType resolveType = handlers.get(elem.getClass());
        if (resolveType == null) {
            Events.ON_UNKNOWN_ELEM.handle(elem);
            return BuiltinTypes.UNDEFINED;
        }
        return resolveType.handle(typeTable, elem);
    }
}
