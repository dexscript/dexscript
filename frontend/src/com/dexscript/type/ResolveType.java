package com.dexscript.type;

import com.dexscript.ast.core.DexElement;
import com.dexscript.ast.type.DexStringLiteralType;
import com.dexscript.ast.type.DexType;
import com.dexscript.ast.type.DexTypeRef;

import java.util.HashMap;
import java.util.Map;

public interface ResolveType {

    interface OnUnknownElem {
        void handle(DexType elem);
    }

    class Events {
        public static OnUnknownElem ON_UNKNOWN_ELEM = elem -> {
            throw new UnsupportedOperationException("not implemented: " + elem.getClass());
        };
    }

    Map<Class<? extends DexElement>, ResolveType> handlers = new HashMap<>() {{
        put(DexTypeRef.class, (typeTable, elem) -> typeTable.resolveType(elem.toString()));
        put(DexStringLiteralType.class, (typeTable, elem) -> {
            String literalValue = ((DexStringLiteralType) (elem)).literalValue();
            return new StringLiteralType(literalValue);
        });
    }};

    Type handle(TopLevelTypeTable typeTable, DexType elem);

    static Type $(TopLevelTypeTable typeTable, DexType elem) {
        ResolveType resolveType = handlers.get(elem.getClass());
        if (resolveType == null) {
            Events.ON_UNKNOWN_ELEM.handle(elem);
            return BuiltinTypes.UNDEFINED;
        }
        return resolveType.handle(typeTable, elem);
    }
}
