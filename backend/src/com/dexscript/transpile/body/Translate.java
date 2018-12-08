package com.dexscript.transpile.body;

import com.dexscript.ast.core.DexElement;
import com.dexscript.ast.expr.DexIntegerLiteral;
import com.dexscript.ast.expr.DexParenExpr;
import com.dexscript.ast.expr.DexStringLiteral;
import com.dexscript.ast.expr.DexValueRef;
import com.dexscript.ast.stmt.DexBlock;
import com.dexscript.ast.stmt.DexStatement;
import com.dexscript.infer.InferValue;
import com.dexscript.infer.Value;
import com.dexscript.transpile.skeleton.OutClass;

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

    Map<Class<? extends DexElement>, Translate> handlers = new HashMap<Class<? extends DexElement>, Translate>() {
        {
            put(DexStringLiteral.class, (oClass, iElem) -> {
                DexStringLiteral iStringLiteral = (DexStringLiteral) iElem;
                iElem.attach(new OutValue("\"" + iStringLiteral.literalValue() + "\""));
            });
            put(DexIntegerLiteral.class, (oClass, iElem) -> {
                DexIntegerLiteral iIntegerLiteral = (DexIntegerLiteral) iElem;
                iElem.attach(new OutValue(iIntegerLiteral.toString() + "L"));
            });
            put(DexValueRef.class, (oClass, iElem) -> {
                Value refValue = InferValue.$(oClass.typeSystem(), (DexValueRef) iElem);
                if (refValue.definedBy() == null) {
                    throw new IllegalStateException("referenced value not found: " + iElem);
                }
                OutValue oValue = refValue.definedBy().attachmentOfType(OutValue.class);
                if (oValue == null) {
                    throw new IllegalStateException("referenced value not translated: " + iElem);
                }
                iElem.attach(oValue);
            });
            put(DexBlock.class, (oClass, iElem) -> {
                DexBlock iBlk = (DexBlock) iElem;
                for (DexStatement stmt : iBlk.stmts()) {
                    Translate.$(oClass, stmt);
                }
            });
            put(DexParenExpr.class, (oClass, iElem) -> {
                DexParenExpr iParenExpr = (DexParenExpr) iElem;
                Translate.$(oClass, iParenExpr.body());
                iElem.attach(iParenExpr.body().attachmentOfType(OutValue.class));
            });
            add(new TranslateReturn());
            add(new TranslateFunctionCall());
            add(new TranslateMethodCall());
            add(new TranslateConsume());
            add(new TranslateProduce());
            add(new TranslateNew());
            add(new TranslateShortVarDecl());
            add(new TranslateAwait());
            add(new TranslateAwaitConsumer());
            add(new TranslateExprStmt());
            add(new TranslateVarDecl());
            add(new TranslateAssign());
            add(new TranslateEqual());
            add(new TranslateIf());
            add(new TranslateElse());
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
