package com.dexscript.infer;

import com.dexscript.ast.expr.DexExpr;
import com.dexscript.ast.expr.DexFunctionCallExpr;
import com.dexscript.ast.expr.DexReference;
import com.dexscript.ast.expr.DexStringLiteral;
import com.dexscript.type.BuiltinTypes;
import com.dexscript.type.StringLiteralType;
import com.dexscript.type.Type;
import com.dexscript.type.TypeSystem;

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
        put(DexReference.class, (ts, elem) -> InferValue.inferValue(ts, (DexReference) elem).type());
        put(DexFunctionCallExpr.class, new InferFunctionCall());
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
