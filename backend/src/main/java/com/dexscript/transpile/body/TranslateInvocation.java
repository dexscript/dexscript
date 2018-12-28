package com.dexscript.transpile.body;

import com.dexscript.ast.core.DexElement;
import com.dexscript.ast.expr.DexExpr;
import com.dexscript.ast.expr.DexInvocation;
import com.dexscript.ast.expr.DexInvocationExpr;
import com.dexscript.ast.expr.DexNamedArg;
import com.dexscript.infer.InferInvocation;
import com.dexscript.runtime.DexRuntimeException;
import com.dexscript.gen.Indent;
import com.dexscript.gen.Line;
import com.dexscript.shim.java.FunctionImpl;
import com.dexscript.transpile.skeleton.OutClass;
import com.dexscript.transpile.skeleton.OutField;
import com.dexscript.transpile.skeleton.OutStateMachine;
import com.dexscript.transpile.skeleton.OutStateMethod;
import com.dexscript.type.*;

public class TranslateInvocation<E extends DexElement & DexInvocationExpr> implements Translate<E> {

    @Override
    public void handle(OutClass oClass, E iElem) {
        DexInvocation invocation = iElem.invocation();
        OutField oResultField = invoke(oClass, invocation);
        if (oResultField != null) {
            iElem.attach(oResultField);
        }
    }

    public static OutField invoke(OutClass oClass, DexInvocation dexIvc) {
        for (DexExpr iPosArg : dexIvc.posArgs()) {
            Translate.$(oClass, iPosArg);
        }
        for (DexNamedArg iNamedArg : dexIvc.namedArgs()) {
            Translate.$(oClass, iNamedArg.val());
        }
        TypeSystem ts = oClass.typeSystem();

        Invocation ivc = InferInvocation.$(ts, dexIvc).requireImpl(true);
        Dispatched dispatched = ts.dispatch(ivc);
        if (dispatched.candidates.isEmpty()) {
            throw new DexRuntimeException("can not find candidates: " + ivc);
        }
        String newF = oClass.oShim().dispatch(ivc.funcName(), ivc.argsCount(), dispatched);
        DType retType = dispatched.ret;

        OutField oActorField = oClass.allocateField(ivc.funcName());
        oClass.g().__(oActorField.value()
        ).__(" = "
        ).__(newF
        ).__("(scheduler");
        for (int i = 0; i < dexIvc.posArgs().size(); i++) {
            oClass.g().__(", ");
            oClass.g().__(Translate.translateExpr(oClass, dexIvc.posArgs().get(i), dispatched.args.get(i)));
        }
        for (int i = 0; i < dispatched.namedArgsMapping.length; i++) {
            int namedArgIndex = dispatched.namedArgsMapping[i];
            oClass.g().__(", ");
            DType targetType = dispatched.args.get(i + dexIvc.posArgs().size());
            oClass.g().__(Translate.translateExpr(oClass, dexIvc.namedArgs().get(namedArgIndex).val(), targetType));
        }
        oClass.g().__(", "
        ).__(Translate.translateContext(dexIvc)
        ).__(new Line(");"));
        boolean needToConsume = needToConsume(dispatched);
        if (needToConsume) {
            return consume(oClass, retType, oActorField.value());
        }
        if (ts.VOID.equals(retType)) {
            return null;
        }
        OutField oResultField = oClass.allocateField(oActorField.value().substring(1) + "Result");
        oClass.g().__(oResultField.value()
        ).__(" = ((Promise)"
        ).__(oActorField.value()
        ).__(new Line(").value();"));
        return oResultField;
    }

    private static boolean needToConsume(Dispatched dispatched) {
        boolean needToConsume = false;
        for (FunctionSig.Invoked candidate : dispatched.candidates) {
            FunctionImpl impl = (FunctionImpl) candidate.func().impl();
            if (impl == null) {
                throw new IllegalStateException("function type defined without impl attached: " + candidate.func());
            }
            if (impl.hasAwait()) {
                needToConsume = true;
                break;
            }
        }
        return needToConsume;
    }

    public static OutField consume(OutClass oClass, DType retType, String targetActor) {
        checkFinished(oClass, targetActor);
        if (retType instanceof VoidType) {
            return null;
        }
        OutField oResultField = oClass.allocateField(targetActor.substring(1) + "Result");
        oClass.g().__(oResultField.value()
        ).__(" = ((Promise)"
        ).__(targetActor
        ).__(new Line(").value();"));
        return oResultField;
    }

    private static void checkFinished(OutClass oClass, String targetActor) {
        OutStateMachine oStateMachine = oClass.oStateMachine();
        int fromState = oStateMachine.state();
        int toState = oStateMachine.nextState();
        oStateMachine.addTransition(fromState, targetActor, toState);
        String stateMethodName = OutStateMethod.methodName(toState);
        oClass.g().__("if (((Promise)"
        ).__(targetActor
        ).__(").finished()) {"
        ).__(new Indent(() -> {
            oClass.g().__(stateMethodName).__("();");
        })).__("} else {"
        ).__(new Indent(() -> {
            oClass.g().__("((Actor)"
            ).__(targetActor
            ).__(").addConsumer(this);");
        })).__(new Line("}"));
        new OutStateMethod(oClass, toState);
    }
}
