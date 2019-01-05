package com.dexscript.type.core;

import com.dexscript.ast.core.DexElement;
import com.dexscript.ast.expr.DexExpr;
import com.dexscript.ast.expr.DexParenExpr;

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
            put(DexParenExpr.class, (ts, elem) -> InferType.$(ts, ((DexParenExpr) elem).body()));
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
