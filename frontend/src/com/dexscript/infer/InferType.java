package com.dexscript.infer;

import com.dexscript.ast.expr.*;
import com.dexscript.type.*;

import java.util.*;

public interface InferType {

    interface OnUnknownExpr {
        void handle(Class<? extends DexExpr> clazz);
    }

    class Events {
        public static OnUnknownExpr ON_UNKNOWN_EXPR = clazz -> {
        };
    }

    Map<Class<? extends DexExpr>, InferType> inferTypes = new HashMap<>() {{
        put(DexStringLiteral.class, (ts, expr) -> new StringLiteralType(((DexStringLiteral) expr).literalValue()));
        put(DexIntegerLiteral.class, (ts, expr) -> new IntegerLiteralType(expr.toString()));
        put(DexReference.class, (ts, expr) -> InferValue.inferValue(ts, (DexReference) expr).type());
        put(DexFunctionCallExpr.class, (ts, expr) -> {
            DexFunctionCallExpr callExpr = (DexFunctionCallExpr) expr;
            String funcName = callExpr.target().asRef().toString();
            List<Type> args = InferType.inferTypes(ts, callExpr.args());
            return ResolveReturnType.$(ts, funcName, args);
        });
        put(DexNewExpr.class, (ts, expr) -> {
            DexNewExpr newExpr = (DexNewExpr) expr;
            String actorName = newExpr.target().asRef().toString();
            StringLiteralType arg1 = new StringLiteralType(actorName);
            List<Type> args = InferType.inferTypes(ts, arg1, newExpr.args());
            return ResolveReturnType.$(ts, "New__", args);
        });
        put(DexConsumeExpr.class, (ts, expr) -> {
            Type target = inferType(ts, ((DexConsumeExpr) expr).right());
            return ResolveReturnType.$(ts, "Consume__", Arrays.asList(target));
        });
    }};

    Type infer(TypeSystem ts, DexExpr expr);

    static Type inferType(TypeSystem ts, DexExpr expr) {
        InferType inferType = inferTypes.get(expr.getClass());
        if (inferType == null) {
            Events.ON_UNKNOWN_EXPR.handle(expr.getClass());
            return BuiltinTypes.UNDEFINED;
        }
        return inferType.infer(ts, expr);
    }

    static List<Type> inferTypes(TypeSystem ts, List<DexExpr> exprs) {
        ArrayList<Type> types = new ArrayList<>();
        for (DexExpr expr : exprs) {
            types.add(InferType.inferType(ts, expr));
        }
        return types;
    }

    static List<Type> inferTypes(TypeSystem ts, Type type1, List<DexExpr> exprs) {
        ArrayList<Type> types = new ArrayList<>();
        types.add(type1);
        for (DexExpr expr : exprs) {
            types.add(InferType.inferType(ts, expr));
        }
        return types;
    }
}
