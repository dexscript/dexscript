package com.dexscript.infer;

import com.dexscript.ast.core.DexElement;
import com.dexscript.ast.expr.*;
import com.dexscript.ast.type.DexParenType;
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
            put(DexParenExpr.class, (ts, elem) -> InferType.$(ts, ((DexParenExpr)elem).body()));
            put(DexStringConst.class, (ts, elem) -> ts.constOf(((DexStringConst) elem).constValue()));
            put(DexIntegerConst.class, (ts, elem) -> ts.constOfInteger(elem.toString()));
            put(DexFloatConst.class, (ts, elem) -> ts.constOfFloat(elem.toString()));
            put(DexBoolConst.class, (ts, elem) -> ts.constOfBool(elem.toString()));
            put(DexValueRef.class, (ts, elem) -> {
                Value val = InferValue.$(ts, (DexValueRef) elem);
                if (val == null) {
                    return ts.UNDEFINED;
                }
                return val.type();
            });
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
            add(new InferInvocation<DexFieldExpr>() {
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
}
