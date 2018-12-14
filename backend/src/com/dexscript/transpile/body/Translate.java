package com.dexscript.transpile.body;

import com.dexscript.ast.core.DexElement;
import com.dexscript.ast.expr.*;
import com.dexscript.ast.stmt.DexBlock;
import com.dexscript.ast.stmt.DexProduceStmt;
import com.dexscript.ast.stmt.DexStatement;
import com.dexscript.infer.InferValue;
import com.dexscript.infer.Value;
import com.dexscript.transpile.skeleton.OutClass;
import com.dexscript.type.JavaSuperTypeArgs;

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
            add(new TranslateInvocation<DexEqualExpr>() {
            });
            add(new TranslateInvocation<DexLessThanExpr>() {
            });
            add(new TranslateInvocation<DexFunctionCallExpr>() {
            });
            add(new TranslateInvocation<DexMethodCallExpr>() {
            });
            add(new TranslateInvocation<DexAddExpr>() {
            });
            add(new TranslateReturn());
            add(new TranslateConsume());
            add(new TranslateProduce());
            add(new TranslateNew());
            add(new TranslateShortVarDecl());
            add(new TranslateAwait());
            add(new TranslateAwaitConsumer());
            add(new TranslateExprStmt());
            add(new TranslateVarDecl());
            add(new TranslateAssign());
            add(new TranslateIf());
            add(new TranslateElse());
            add(new TranslateIncr());
            add(new TranslateFor());
        }

        private void add(Translate<?> handler) {
            put((Class<? extends DexElement>) JavaSuperTypeArgs.$(handler.getClass())[0], handler);
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
