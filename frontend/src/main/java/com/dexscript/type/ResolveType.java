package com.dexscript.type;

import com.dexscript.ast.core.DexElement;
import com.dexscript.ast.type.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// ResolveType translate AST to type object in type system
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
        put(DexVoidType.class, (ts, localTypeTable, elem) -> ts.VOID);
        put(DexTypeRef.class, (ts, localTypeTable, elem) -> {
            String name = elem.toString();
            if (localTypeTable != null) {
                DType type = localTypeTable.resolveType(elem.pkg(), name);
                if (!(type instanceof UndefinedType)) {
                    return type;
                }
            }
            return ts.typeTable().resolveType(elem.pkg(), name);
        });
        put(DexStringLiteralType.class, (ts, localTypeTable, elem) -> {
            String literalValue = ((DexStringLiteralType) (elem)).literalValue();
            return new StringLiteralType(ts, literalValue);
        });
        put(DexIntegerLiteralType.class, (ts, localTypeTable, elem) ->
                new IntegerLiteralType(ts, elem.toString()));
        put(DexBoolLiteralType.class, (ts, localTypeTable, elem) ->
                new BoolLiteralType(ts, elem.toString()));
        put(DexParameterizedType.class, (ts, localTypeTable, elem) -> {
            DexParameterizedType genericExpansionType = (DexParameterizedType) elem;
            List<DType> typeArgs = new ArrayList<>();
            for (DexType typeArg : genericExpansionType.typeArgs()) {
                typeArgs.add(ResolveType.$(ts, localTypeTable, typeArg));
            }
            String genericTypeName = genericExpansionType.genericType().toString();
            return ts.typeTable().resolveType(elem.pkg(), genericTypeName, typeArgs);
        });
        put(DexInterfaceType.class, (ts, localTypeTable, elem) -> {
            DexInterfaceType infType = (DexInterfaceType) elem;
            if (infType.functions().isEmpty() && infType.methods().isEmpty()) {
                return ts.ANY;
            }
            throw new UnsupportedOperationException("not implemented");
        });
        put(DexIntersectionType.class, (ts, localTypeTable, elem) -> {
            DexIntersectionType intersectionType = (DexIntersectionType) elem;
            DType left = ResolveType.$(ts, localTypeTable, intersectionType.left());
            DType right = ResolveType.$(ts, localTypeTable, intersectionType.right());
            return left.intersect(right);
        });
        put(DexUnionType.class, (ts, localTypeTable, elem) -> {
            DexUnionType unionType = (DexUnionType) elem;
            DType left = ResolveType.$(ts, localTypeTable, unionType.left());
            DType right = ResolveType.$(ts, localTypeTable, unionType.right());
            return left.union(right);
        });
        put(DexParenType.class, (ts, localTypeTable, elem) -> ResolveType.$(
                ts, localTypeTable, ((DexParenType)elem).body()));
    }};

    DType handle(TypeSystem ts, TypeTable localTypeTable, E elem);

    static DType $(TypeSystem ts, String typeDef) {
        return ResolveType.$(ts, null, DexType.$parse(typeDef));
    }

    static DType $(TypeSystem ts, TypeTable localTypeTable, DexType elem) {
        ResolveType resolveType = handlers.get(elem.getClass());
        if (resolveType == null) {
            Events.ON_UNKNOWN_ELEM.handle(elem);
            return ts.UNDEFINED;
        }
        return resolveType.handle(ts, localTypeTable, elem);
    }

    static List<DType> resolveTypes(TypeSystem ts, String... typeDefs) {
        List<DType> types = new ArrayList<>();
        for (String typeDef : typeDefs) {
            types.add(ResolveType.$(ts, typeDef));
        }
        return types;
    }

    static List<DType> resolveTypes(TypeSystem ts, TypeTable localTypeTable, List<DexType> dexTypes) {
        List<DType> types = new ArrayList<>();
        for (DexType dexType : dexTypes) {
            types.add(ResolveType.$(ts, localTypeTable, dexType));
        }
        return types;
    }
}
