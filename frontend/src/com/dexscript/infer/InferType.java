package com.dexscript.infer;

import com.dexscript.ast.core.DexElement;
import com.dexscript.ast.expr.*;
import com.dexscript.type.*;

import java.util.*;


public interface InferType<E extends DexExpr> {

    interface OnUnknownElem {
        void handle(DexExpr elem);
    }

    class Events {
        public static OnUnknownElem ON_UNKNOWN_ELEM = elem -> {
            throw new UnsupportedOperationException("not implemented: " + elem.getClass());
        };
    }

    Map<Class<? extends DexElement>, InferType> handlers = new HashMap<Class<? extends DexElement>, InferType>() {
        {
            put(DexStringLiteral.class, (ts, elem) -> new StringLiteralType(ts, ((DexStringLiteral) elem).literalValue()));
            put(DexIntegerConst.class, (ts, elem) -> new IntegerConstType(ts, elem.toString()));
            put(DexValueRef.class, (ts, elem) -> InferValue.$(ts, (DexValueRef) elem).type());
            add(new InferInvocation<DexConsumeExpr>() {
            });
            add(new InferInvocation<DexNewExpr>() {
            });
            add(new InferInvocation<DexEqualExpr>() {
            });
            add(new InferInvocation<DexLessThanExpr>() {
            });
            add(new InferInvocation<DexMethodCallExpr>() {
            });
            add(new InferInvocation<DexFunctionCallExpr>() {
            });
            add(new InferInvocation<DexAddExpr>() {
            });
            add(new InferInvocation<DexIndexExpr>() {
            });
        }

        private void add(InferType<?> handler) {
            put((Class<? extends DexExpr>) JavaSuperTypeArgs.$(handler.getClass())[0], handler);
        }
    };

    DType handle(TypeSystem ts, E elem);

    static DType $(TypeSystem ts, DexExpr elem) {
        InferType inferType = handlers.get(elem.getClass());
        if (inferType == null) {
            Events.ON_UNKNOWN_ELEM.handle(elem);
            return ts.UNDEFINED;
        }
        return inferType.handle(ts, elem);
    }

    static List<DType> inferTypes(TypeSystem ts, List<DexExpr> elems) {
        ArrayList<DType> types = new ArrayList<>();
        for (DexExpr elem : elems) {
            types.add(InferType.$(ts, elem));
        }
        return types;
    }

    static List<DType> inferTypes(TypeSystem ts, DType type1, List<DexExpr> elems) {
        ArrayList<DType> types = new ArrayList<>();
        types.add(type1);
        for (DexExpr elem : elems) {
            types.add(InferType.$(ts, elem));
        }
        return types;
    }

}
