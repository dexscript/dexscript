package com.dexscript.transpile.body;

import com.dexscript.ast.core.DexElement;
import com.dexscript.ast.expr.*;
import com.dexscript.ast.stmt.DexBlock;
import com.dexscript.ast.stmt.DexStatement;
import com.dexscript.infer.InferType;
import com.dexscript.infer.InferValue;
import com.dexscript.infer.Value;
import com.dexscript.transpile.skeleton.OutClass;
import com.dexscript.type.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
            put(DexStringConst.class, (oClass, iElem) -> {
                DexStringConst iStringConst = (DexStringConst) iElem;
                iElem.attach(new OutValue("\"" + iStringConst.constValue() + "\""));
            });
            put(DexIntegerConst.class, (oClass, iElem) -> {
                DexIntegerConst iIntegerConst = (DexIntegerConst) iElem;
                iElem.attach(new OutValue(iIntegerConst.toString() + "L"));
            });
            put(DexBoolConst.class, (oClass, iElem) -> {
                DexBoolConst iBoolConst = (DexBoolConst) iElem;
                iElem.attach(new OutValue(iBoolConst.toString()));
            });
            put(DexFloatConst.class, (oClass, iElem) -> {
                DexFloatConst iFloatConst = (DexFloatConst) iElem;
                iElem.attach(new OutValue(iFloatConst.toString() + "D"));
            });
            put(DexValueRef.class, (oClass, iElem) -> {
                if (iElem.toString().equals("$")) {
                    iElem.attach(new OutValue("context"));
                    return;
                }
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
            add(new TranslateInvocation<DexIndexExpr>() {
            });
            add(new TranslateInvocation<DexFieldExpr>() {
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

    static String translateExpr(OutClass oClass, DexExpr iExpr, DType targetType) {
        Translate.$(oClass, iExpr);
        String val = OutValue.of(iExpr);
        TypeSystem ts = oClass.typeSystem();
        if (ts.isIntegerConst(InferType.$(ts, iExpr))) {
            if (ts.isInt32(targetType)) {
                return "Integer.valueOf((int)" + val + ")";
            } else if (ts.isFloat64(targetType)) {
                return "Double.valueOf((double)" + val + ")";
            } else if (ts.isFloat32(targetType)) {
                return "Float.valueOf((float)" + val + ")";
            } else if (ts.isInt64(targetType) || ts.isIntegerLiteral(targetType)) {
                // keep it as Long
                return val;
            }
            throw new UnsupportedOperationException("not implemented");
        }
        if (ts.isFloatConst(InferType.$(ts, iExpr))) {
            if (ts.isFloat32(targetType)) {
                return "Float.valueOf((float)" + val + ")";
            } else if (ts.isFloat64(targetType)) {
                // keep it as Double
                return val;
            }
            throw new UnsupportedOperationException("not implemented");
        }
        return val;
    }

    static String translateContext(OutClass oClass, DexInvocation ivc) {
        DexExpr context = ivc.context();
        if (context == null) {
            return "context";
        }
        Translate.$(oClass, context);
        return OutValue.of(context);
    }

    @NotNull
    static List<String> translateArgs(OutClass oClass, DexInvocation dexIvc, Dispatched dispatched) {
        List<String> translatedArgs = new ArrayList<>();
        for (int i = 0; i < dexIvc.posArgs().size(); i++) {
            String translatedArg = Translate.translateExpr(oClass, dexIvc.posArgs().get(i), dispatched.args.get(i));
            translatedArgs.add(translatedArg);
        }
        for (int i = 0; i < dispatched.namedArgsMapping.length; i++) {
            int namedArgIndex = dispatched.namedArgsMapping[i];
            DType targetType = dispatched.args.get(i + dexIvc.posArgs().size());
            String translatedArg = Translate.translateExpr(oClass, dexIvc.namedArgs().get(namedArgIndex).val(), targetType);
            translatedArgs.add(translatedArg);
        }
        translatedArgs.add(Translate.translateContext(oClass, dexIvc));
        return translatedArgs;
    }
}
