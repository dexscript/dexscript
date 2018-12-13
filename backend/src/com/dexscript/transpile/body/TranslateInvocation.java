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
import com.dexscript.transpile.shim.impl.FunctionImpl;
import com.dexscript.transpile.skeleton.OutClass;
import com.dexscript.transpile.skeleton.OutField;
import com.dexscript.transpile.skeleton.OutStateMachine;
import com.dexscript.transpile.skeleton.OutStateMethod;
import com.dexscript.type.*;

import java.util.Arrays;
import java.util.List;

public class TranslateInvocation<E extends DexElement & DexInvocationExpr> implements Translate<E> {

    @Override
    public void handle(OutClass oClass, E iElem) {
        DexInvocation invocation = iElem.invocation();
        OutField oResultField = invoke(oClass, invocation.funcName(), invocation.typeArgs(), invocation.args());
        if (oResultField != null) {
            iElem.attach(oResultField);
        }
    }

    public static OutField invoke(OutClass oClass, String funcName, List<DexType> iTypeArgs, List<DexExpr> iArgs) {
        for (DexExpr iArg : iArgs) {
            Translate.$(oClass, iArg);
        }
        TypeSystem ts = oClass.typeSystem();

        List<Type> args = InferType.inferTypes(ts, iArgs);
        List<Type> typeArgs = ts.resolveTypes(iTypeArgs);
        List<FunctionType.Invoked> invokeds = ts.invoke(funcName, typeArgs, args, null);
        if (invokeds.size() == 0) {
            throw new DexRuntimeException(String.format("can not resolve implementation of function %s with %s",
                    funcName, args));
        }
        String newF = oClass.oShim().combineNewF(funcName, iArgs.size(), invokeds);
        Type retType = ResolveReturnType.$(invokeds);

        Type promiseType = ts.resolveType("Promise", Arrays.asList(retType));
        OutField oActorField = oClass.allocateField(funcName, promiseType);
        oClass.g().__(oActorField.value()
        ).__(" = "
        ).__(newF
        ).__("(scheduler");
        for (int i = 0; i < iArgs.size(); i++) {
            oClass.g().__(", ");
            DexExpr iArg = iArgs.get(i);
            OutValue oValue = iArg.attachmentOfType(OutValue.class);
            oClass.g().__(oValue.value());
        }
        oClass.g().__(new Line(");"));
        boolean needToConsume = false;
        for (FunctionType.Invoked invoked : invokeds) {
            FunctionImpl impl = (FunctionImpl) invoked.function().attachment();
            if (impl.hasAwait()) {
                needToConsume =  true;
                break;
            }
        }
        if (needToConsume) {
            return consume(oClass, retType, oActorField.value());
        }
        if (BuiltinTypes.VOID.equals(retType)) {
            return null;
        }
        OutField oResultField = oClass.allocateField(oActorField.value().substring(1) + "Result", retType);
        oClass.g().__(oResultField.value()
        ).__(" = (("
        ).__(retType.javaClassName()
        ).__(")("
        ).__(oActorField.value()
        ).__(new Line(".value()));"));
        return oResultField;
    }

    public static OutField consume(OutClass oClass, Type retType, String targetActor) {
        checkFinished(oClass, targetActor);
        if (BuiltinTypes.VOID.equals(retType)) {
            return null;
        }
        OutField oResultField = oClass.allocateField(targetActor.substring(1) + "Result", retType);
        oClass.g().__(oResultField.value()
        ).__(" = (("
        ).__(retType.javaClassName()
        ).__(")("
        ).__(targetActor
        ).__(new Line(".value()));"));
        return oResultField;
    }

    private static void checkFinished(OutClass oClass, String targetActor) {
        OutStateMachine oStateMachine = oClass.oStateMachine();
        int fromState = oStateMachine.state();
        int toState = oStateMachine.nextState();
        oStateMachine.addTransition(fromState, targetActor, toState);
        String stateMethodName = OutStateMethod.methodName(toState);
        oClass.g().__("if ("
        ).__(targetActor
        ).__(".finished()) {"
        ).__(new Indent(() -> {
            oClass.g().__(stateMethodName).__("();");
        })
        ).__("} else {"
        ).__(new Indent(() -> {
            oClass.g().__("((Actor)"
            ).__(targetActor
            ).__(").addConsumer(this);");
        })
        ).__(new Line("}"));
        new OutStateMethod(oClass, toState);
    }
}
