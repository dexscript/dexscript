package com.dexscript.transpile.elem;

import com.dexscript.ast.core.DexElement;
import com.dexscript.ast.expr.DexStringLiteral;
import com.dexscript.ast.expr.DexValueRef;
import com.dexscript.infer.InferValue;
import com.dexscript.infer.Value;
import com.dexscript.transpile.OutClass;
import com.dexscript.transpile.OutValue;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Map;

public interface Translate<E extends DexElement> {

    interface OnUnknownElem {
        void handle(DexElement iElem);
    }

    class Events {
        public static OnUnknownElem ON_UNKNOWN_ELEM = iElem -> {
            throw new UnsupportedOperationException("not implemented: " + iElem.getClass());
        };
    }

    Map<Class<? extends DexElement>, Translate> handlers = new HashMap<>() {
        {
            put(DexStringLiteral.class, ((oClass, iElem) -> {
                DexStringLiteral iStringLiteral = (DexStringLiteral) iElem;
                iStringLiteral.attach(new OutValue("\"" + iStringLiteral.literalValue() + "\""));
            }));
            put(DexValueRef.class, (oClass, iElem) -> {
                Value refValue = InferValue.$(oClass.typeSystem(), (DexValueRef) iElem);
                OutValue oValue = refValue.definedBy().attachmentOfType(OutValue.class);
                iElem.attach(oValue);
            });
            add(new TranslateReturn());
            add(new TranslateFunctionCall());
            add(new TranslateMethodCall());
            add(new TranslateConsume());
            add(new TranslateNew());
            add(new TranslateShortVarDecl());
            add(new TranslateAwait());
            add(new TranslateAwaitConsumer());
            add(new TranslateExprStmt());
        }

        private void add(Translate<?> handler) {
            ParameterizedType translateInf = (ParameterizedType) handler.getClass().getGenericInterfaces()[0];
            put((Class<? extends DexElement>) translateInf.getActualTypeArguments()[0], handler);
        }
    };

    void handle(OutClass oClass, E iElem);

    static void $(OutClass oClass, DexElement iElem) {
        Translate translate = handlers.get(iElem.getClass());
        if (translate == null) {
            Events.ON_UNKNOWN_ELEM.handle(iElem);
            return;
        }
        translate.handle(oClass, iElem);
    }
}
