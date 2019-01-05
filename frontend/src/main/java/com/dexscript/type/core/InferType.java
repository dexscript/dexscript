package com.dexscript.type.core;

import com.dexscript.ast.DexPackage;
import com.dexscript.ast.core.DexElement;
import com.dexscript.ast.expr.DexExpr;
import com.dexscript.ast.expr.DexParenExpr;
import com.dexscript.ast.type.DexParameterizedType;
import com.dexscript.ast.type.DexParenType;
import com.dexscript.ast.type.DexType;
import com.dexscript.ast.type.DexTypeRef;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public interface InferType<E extends DexElement> {

    interface OnUnknownElem {
        void handle(DexElement elem);
    }

    class Events {
        public static OnUnknownElem ON_UNKNOWN_ELEM = elem -> {
            throw new UnsupportedOperationException("not implemented: " + elem.getClass());
        };
    }

    Map<Class<? extends DexElement>, InferType> handlers = new HashMap<Class<? extends DexElement>, InferType>() {
        {
            put(DexParenExpr.class, (ts, localTypeTable, elem) -> InferType.$(
                    ts, localTypeTable, ((DexParenExpr) elem).body()));
            put(DexParenType.class, (ts, localTypeTable, elem) -> $(ts, localTypeTable, ((DexParenType)elem).body()));
            put(DexTypeRef.class, (ts, localTypeTable, elem) -> {
                DexTypeRef typeRef = (DexTypeRef) elem;
                DexPackage pkg = elem.pkg();
                if (!typeRef.pkgName().isEmpty()) {
                    pkg = new DexPackage(typeRef.pkgName());
                }
                String name = typeRef.typeName();
                if (localTypeTable != null) {
                    DType type = localTypeTable.resolveType(pkg, name);
                    if (!(type instanceof UndefinedType)) {
                        return type;
                    }
                }
                return ts.typeTable().resolveType(pkg, name);
            });
            put(DexParameterizedType.class, (ts, localTypeTable, elem) -> {
                DexParameterizedType parameterizedType = (DexParameterizedType) elem;
                List<DType> typeArgs = new ArrayList<>();
                for (DexType typeArg : parameterizedType.typeArgs()) {
                    typeArgs.add($(ts, localTypeTable, typeArg));
                }
                DType genericType = $(ts, localTypeTable, parameterizedType.genericType());
                return ts.typeTable().resolveType(genericType, typeArgs);
            });
        }
    };

    DType handle(TypeSystem ts, TypeTable localTypeTable, E elem);

    static DType $(TypeSystem ts, DexElement elem) {
        return InferType.$(ts, null, elem);
    }

    static DType $(TypeSystem ts, TypeTable localTypeTable, DexElement elem) {
        DType type = elem.attachmentOfType(DType.class);
        if (type != null) {
            return type;
        }
        InferType inferType = handlers.get(elem.getClass());
        if (inferType == null) {
            Events.ON_UNKNOWN_ELEM.handle(elem);
            return ts.UNDEFINED;
        }
        type = inferType.handle(ts, localTypeTable, elem);
        elem.attach(type);
        return type;
    }

    static List<DType> inferTypes(TypeSystem ts, List<DexExpr> elems) {
        ArrayList<DType> types = new ArrayList<>();
        for (DexExpr elem : elems) {
            types.add(InferType.$(ts, elem));
        }
        return types;
    }
}
