package com.dexscript.type;

import com.dexscript.ast.core.DexElement;
import com.dexscript.ast.elem.DexParam;
import com.dexscript.ast.type.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
        put(DexVoidType.class, (typeTable, elem) -> BuiltinTypes.VOID);
        put(DexTypeRef.class, (typeTable, elem) -> typeTable.resolveType(elem.toString()));
        put(DexStringLiteralType.class, (typeTable, elem) -> {
            String literalValue = ((DexStringLiteralType) (elem)).literalValue();
            return new StringLiteralType(literalValue);
        });
        put(DexGenericExpansionType.class, (typeTable, elem) -> {
            DexGenericExpansionType genericExpansionType = (DexGenericExpansionType) elem;
            List<Type> typeArgs = new ArrayList<>();
            for (DexType typeArg : genericExpansionType.typeArgs()) {
                typeArgs.add(ResolveType.$(typeTable, typeArg));
            }
            return typeTable.resolveType(genericExpansionType.genericType().toString(), typeArgs);
        });
        put(DexInterfaceType.class, (typeTable, elem) -> {
            DexInterfaceType infType = (DexInterfaceType) elem;
            if (infType.functions().isEmpty() && infType.methods().isEmpty()) {
                return BuiltinTypes.ANY;
            }
            throw new UnsupportedOperationException("not implemented");
        });
    }};

    Type handle(TypeTable typeTable, E elem);

    static Type $(TypeTable typeTable, DexType elem) {
        ResolveType resolveType = handlers.get(elem.getClass());
        if (resolveType == null) {
            Events.ON_UNKNOWN_ELEM.handle(elem);
            return BuiltinTypes.UNDEFINED;
        }
        return resolveType.handle(typeTable, elem);
    }

    static List<Type> $(TypeTable typeTable, String... typeDefs) {
        List<Type> types = new ArrayList<>();
        for (String typeDef : typeDefs) {
            DexType dexType = DexType.parse(typeDef);
            types.add(ResolveType.$(typeTable, dexType));
        }
        return types;
    }

    static List<Type> resolveParams(TypeTable typeTable, List<DexParam> params) {
        List<Type> types = new ArrayList<>();
        for (DexParam param : params) {
            types.add(ResolveType.$(typeTable, param.paramType()));
        }
        return types;
    }

    static List<Type> resolveTypes(TypeTable typeTable, List<DexType> dexTypes) {
        List<Type> types = new ArrayList<>();
        for (DexType dexType : dexTypes) {
            types.add(ResolveType.$(typeTable, dexType));
        }
        return types;
    }
}
