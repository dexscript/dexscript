package com.dexscript.infer;

import com.dexscript.ast.expr.*;
import com.dexscript.type.*;

import java.util.HashMap;
import java.util.Map;

public interface InferType {

    interface OnUnknownExpr {
        void handle(Class<? extends DexExpr> clazz);
    }

    class Events {
        public static OnUnknownExpr ON_UNKNOWN_EXPR = clazz -> {
        };
    }

    Map<Class<? extends DexExpr>, InferType> inferTypes = new HashMap<>() {{
        put(DexStringLiteral.class, (ts, elem) -> new StringLiteralType(((DexStringLiteral) elem).literalValue()));
        put(DexIntegerLiteral.class, (ts, elem) -> new IntegerLiteralType(elem.toString()));
        put(DexReference.class, (ts, elem) -> InferValue.inferValue(ts, (DexReference) elem).type());
        put(DexFunctionCallExpr.class, new InferFunctionCall());
        put(DexNewExpr.class, new InferNew());
    }};

    Type infer(TypeSystem ts, DexExpr elem);

    static Type inferType(TypeSystem ts, DexExpr elem) {
        InferType inferType = inferTypes.get(elem.getClass());
        if (inferType == null) {
            Events.ON_UNKNOWN_EXPR.handle(elem.getClass());
            return BuiltinTypes.UNDEFINED;
        }
        return inferType.infer(ts, elem);
    }
}
