package com.dexscript.transpile.body;

import com.dexscript.ast.expr.DexExpr;
import com.dexscript.ast.expr.DexFunctionCallExpr;
import com.dexscript.infer.InferType;
import com.dexscript.transpile.gen.Indent;
import com.dexscript.transpile.skeleton.OutClass;
import com.dexscript.transpile.skeleton.OutField;
import com.dexscript.transpile.gen.Line;
import com.dexscript.transpile.skeleton.OutStateMachine;
import com.dexscript.transpile.skeleton.OutStateMethod;
import com.dexscript.type.*;

import java.util.List;

public class TranslateFunctionCall implements Translate<DexFunctionCallExpr> {

    @Override
    public void handle(OutClass oClass, DexFunctionCallExpr iCallExpr) {
        String funcName = iCallExpr.target().asRef().toString();
        handle(oClass, iCallExpr, funcName, iCallExpr.args());
    }

    public static void handle(OutClass oClass, DexExpr iElem, String funcName, List<DexExpr> iArgs) {
        for (DexExpr iArg : iArgs) {
            Translate.$(oClass, iArg);
        }
        TypeSystem ts = oClass.typeSystem();

        List<FunctionType> funcTypes = ts.resolveFunctions(funcName, InferType.inferTypes(ts, iArgs));
        String newF = oClass.oShim().combineNewF(funcName, iArgs.size(), funcTypes);

        Type promiseType = ts.resolveType("Promise");
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

        consume(oClass, iElem, oActorField.value());
    }

    public static void consume(OutClass oClass, DexExpr iElem, String targetActor) {
        checkFinished(oClass, targetActor);
        Type resultType = InferType.$(oClass.typeSystem(), iElem);
        if (BuiltinTypes.VOID.equals(resultType)) {
            return;
        }
        OutField oResultField = oClass.allocateField(targetActor.substring(1) + "Result", resultType);
        oClass.g().__(oResultField.value()
        ).__(" = (("
        ).__(resultType.javaClassName()
        ).__(")("
        ).__(targetActor
        ).__(new Line(".value()));"));
        iElem.attach(oResultField);
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
