package com.dexscript.transpile.body;

import com.dexscript.ast.core.DexElement;
import com.dexscript.ast.expr.DexExpr;
import com.dexscript.ast.expr.DexInvocation;
import com.dexscript.ast.expr.DexInvocationExpr;
import com.dexscript.ast.type.DexType;
import com.dexscript.infer.InferType;
import com.dexscript.runtime.DexRuntimeException;
import com.dexscript.transpile.gen.Indent;
import com.dexscript.transpile.gen.Line;
import com.dexscript.transpile.type.java.FunctionImpl;
import com.dexscript.transpile.skeleton.OutClass;
import com.dexscript.transpile.skeleton.OutField;
import com.dexscript.transpile.skeleton.OutStateMachine;
import com.dexscript.transpile.skeleton.OutStateMethod;
import com.dexscript.type.*;

import java.util.List;

public class TranslateInvocation<E extends DexElement & DexInvocationExpr> implements Translate<E> {

    @Override
    public void handle(OutClass oClass, E iElem) {
        DexInvocation invocation = iElem.invocation();
        OutField oResultField = invoke(oClass, invocation.funcName(), invocation.typeArgs(), invocation.posArgs());
        if (oResultField != null) {
            iElem.attach(oResultField);
        }
    }

    public static OutField invoke(OutClass oClass, String funcName, List<DexType> iTypeArgs, List<DexExpr> iArgs) {
        for (DexExpr iArg : iArgs) {
            Translate.$(oClass, iArg);
        }
        TypeSystem ts = oClass.typeSystem();

        List<DType> args = InferType.inferTypes(ts, iArgs);
        List<DType> typeArgs = ResolveType.resolveTypes(ts, null, iTypeArgs);
        Invocation ivc = new Invocation(funcName, typeArgs, args, null)
                .requireImpl(true);
        Invoked invoked = ts.invoke(ivc);
        if (invoked.candidates.size() == 0) {
            throw new DexRuntimeException(String.format("can not resolve implementation of function %s with %s",
                    funcName, args));
        }
        String newF = oClass.oShim().dispatch(funcName, iArgs.size(), invoked);
        DType retType = ResolveReturnType.$(invoked);

        OutField oActorField = oClass.allocateField(funcName);
        oClass.g().__(oActorField.value()
        ).__(" = "
        ).__(newF
        ).__("(scheduler");
        for (int i = 0; i < iArgs.size(); i++) {
            oClass.g().__(", ");
            oClass.g().__(Translate.translateExpr(oClass, iArgs.get(i), invoked.args.get(i)));
        }
        oClass.g().__(new Line(");"));
        boolean needToConsume = needToConsume(invoked);
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

    private static boolean needToConsume(Invoked invoked) {
        boolean needToConsume = false;
        for (FunctionSig.Invoked candidate : invoked.candidates) {
            FunctionImpl impl = (FunctionImpl) candidate.function().impl();
            if (impl == null) {
                throw new IllegalStateException("function type defined without impl attached: " + candidate.function());
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
