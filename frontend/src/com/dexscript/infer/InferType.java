package com.dexscript.infer;

import com.dexscript.ast.core.DexElement;
import com.dexscript.ast.expr.*;
import com.dexscript.ast.type.DexType;
import com.dexscript.type.*;

import java.util.*;

public interface InferType {

    interface OnUnknownElem {
        void handle(DexExpr elem);
    }

    class Events {
        public static OnUnknownElem ON_UNKNOWN_ELEM = elem -> {
            throw new UnsupportedOperationException("not implemented: " + elem.getClass());
        };
    }

    Map<Class<? extends DexElement>, InferType> handlers = new HashMap<>() {{
        put(DexStringLiteral.class, (ts, elem) -> new StringLiteralType(((DexStringLiteral) elem).literalValue()));
        put(DexIntegerLiteral.class, (ts, elem) -> new IntegerLiteralType(elem.toString()));
        put(DexValueRef.class, (ts, elem) -> InferValue.$(ts, (DexValueRef) elem).type());
        put(DexFunctionCallExpr.class, (ts, elem) -> {
            DexFunctionCallExpr callExpr = (DexFunctionCallExpr) elem;
            String funcName = callExpr.target().asRef().toString();
            List<Type> args = InferType.inferTypes(ts, callExpr.args());
            return ResolveReturnType.$(ts, funcName, args);
        });
        put(DexNewExpr.class, (ts, elem) -> {
            DexNewExpr newExpr = (DexNewExpr) elem;
            String actorName = newExpr.target().asRef().toString();
            StringLiteralType arg1 = new StringLiteralType(actorName);
            List<Type> args = InferType.inferTypes(ts, arg1, newExpr.args());
            return ResolveReturnType.$(ts, "New__", args);
        });
        put(DexConsumeExpr.class, (ts, elem) -> {
            Type target = $(ts, ((DexConsumeExpr) elem).right());
            return ResolveReturnType.$(ts, "Consume__", Arrays.asList(target));
        });
    }};

    Type handle(TypeSystem ts, DexExpr elem);

    static Type $(TypeSystem ts, DexExpr elem) {
        InferType inferType = handlers.get(elem.getClass());
        if (inferType == null) {
            Events.ON_UNKNOWN_ELEM.handle(elem);
            return BuiltinTypes.UNDEFINED;
        }
        return inferType.handle(ts, elem);
    }

    static Type $(TypeSystem ts, DexType elem) {
        return ResolveType.$(ts.typeTable(), elem);
    }

    static List<Type> inferTypes(TypeSystem ts, List<DexExpr> elems) {
        ArrayList<Type> types = new ArrayList<>();
        for (DexExpr elem : elems) {
            types.add(InferType.$(ts, elem));
        }
        return types;
    }

    static List<Type> inferTypes(TypeSystem ts, Type type1, List<DexExpr> elems) {
        ArrayList<Type> types = new ArrayList<>();
        types.add(type1);
        for (DexExpr elem : elems) {
            types.add(InferType.$(ts, elem));
        }
        return types;
    }
}
