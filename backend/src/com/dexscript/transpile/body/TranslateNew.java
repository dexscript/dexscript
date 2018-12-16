package com.dexscript.transpile.body;

import com.dexscript.ast.expr.DexExpr;
import com.dexscript.ast.expr.DexInvocation;
import com.dexscript.ast.expr.DexNewExpr;
import com.dexscript.infer.InferType;
import com.dexscript.runtime.DexRuntimeException;
import com.dexscript.transpile.gen.Gen;
import com.dexscript.transpile.gen.Line;
import com.dexscript.transpile.skeleton.OutClass;
import com.dexscript.transpile.skeleton.OutField;
import com.dexscript.type.*;

import java.util.Arrays;
import java.util.List;

public class TranslateNew implements Translate<DexNewExpr> {

    public interface OnFunctionMissing {
        void handle(DexNewExpr iNewExpr);
    }

    public static OnFunctionMissing ON_FUNCTION_MISSING = iNewExpr -> {
        throw new DexRuntimeException("function not found: " + iNewExpr);
    };

    @Override
    public void handle(OutClass oClass, DexNewExpr iNewExpr) {
        List<DexExpr> iArgs = iNewExpr.args();
        for (DexExpr iArg : iArgs) {
            Translate.$(oClass, iArg);
        }

        String funcName = iNewExpr.target().asRef().toString();
        TypeSystem ts = oClass.typeSystem();

        DexInvocation invocation = iNewExpr.invocation();
        List<DType> args = InferType.inferTypes(ts, invocation.args());
        List<DType> typeArgs = ts.resolveTypes(invocation.typeArgs());
        List<FunctionSig.Invoked> invokeds = ts.invoke(new Invocation("New__", typeArgs, args, null));
        if (invokeds.isEmpty()) {
            ON_FUNCTION_MISSING.handle(iNewExpr);
        }
        String newF = oClass.oShim().dispatch(funcName, args.size(), invokeds);
        DType retType = ResolveReturnType.$(invokeds);

        DType promiseType = ts.resolveType("Promise", Arrays.asList(retType));
        OutField oActorField = oClass.allocateField(funcName, promiseType);
        Gen g = oClass.g();
        g.__(oActorField.value()
        ).__(" = "
        ).__(newF
        ).__("(scheduler, \""
        ).__(funcName
        ).__("\"");
        for (int i = 0; i < iArgs.size(); i++) {
            g.__(", ");
            DexExpr iArg = iArgs.get(i);
            OutValue oValue = iArg.attachmentOfType(OutValue.class);
            g.__(oValue.value());
        }
        g.__(new Line(");"));
        iNewExpr.attach(oActorField);
    }
}
