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
            put(DexParenExpr.class, (ts, localTypeTable, elem) -> InferType.$(ts, ((DexParenExpr) elem).body()));
        }
    };

    DType handle(TypeSystem ts, TypeTable localTypeTable, E elem);

    static DType $(TypeSystem ts, DexExpr elem) {
        return InferType.$(ts, null, elem);
    }

    static DType $(TypeSystem ts, TypeTable localTypeTable, DexExpr elem) {
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
