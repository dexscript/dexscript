package com.dexscript.transpile.elem;

import com.dexscript.ast.core.DexElement;
import com.dexscript.ast.expr.*;
import com.dexscript.ast.func.DexReturnStmt;
import com.dexscript.infer.InferValue;
import com.dexscript.infer.Value;
import com.dexscript.transpile.OutClass;
import com.dexscript.transpile.OutValue;

import java.util.HashMap;
import java.util.Map;

public interface Translate {

    interface OnUnknownElem {
        void handle(DexElement iElem);
    }

    class Events {
        public static OnUnknownElem ON_UNKNOWN_ELEM = iElem -> {
            throw new UnsupportedOperationException("not implemented: " + iElem.getClass());
        };
    }

    Map<Class<? extends DexElement>, Translate> handlers = new HashMap<>() {{
        put(DexReturnStmt.class, new TranslateReturn());
        put(DexStringLiteral.class, new TranslateStringLiteral());
        put(DexFunctionCallExpr.class, new TranslateFunctionCall());
        put(DexValueRef.class, (oClass, iElem) -> {
            Value refValue = InferValue.inferValue(oClass.typeSystem(), (DexValueRef) iElem);
            OutValue oValue = refValue.definedBy().attachmentOfType(OutValue.class);
            iElem.attach(oValue);
        });
        put(DexConsumeExpr.class, new TranslateConsume());
        put(DexNewExpr.class, new TranslateNew());
    }};

    void handle(OutClass oClass, DexElement iElem);

    static void $(OutClass oClass, DexElement iElem) {
        Translate translate = handlers.get(iElem.getClass());
        if (translate == null) {
            Events.ON_UNKNOWN_ELEM.handle(iElem);
            return;
        }
        translate.handle(oClass, iElem);
    }
}
