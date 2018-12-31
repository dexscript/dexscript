package com.dexscript.transpile.body;

import com.dexscript.ast.expr.DexExpr;
import com.dexscript.ast.expr.DexInvocation;
import com.dexscript.ast.expr.DexNewExpr;
import com.dexscript.infer.InferInvocation;
import com.dexscript.runtime.DexRuntimeException;
import com.dexscript.gen.Gen;
import com.dexscript.gen.Line;
import com.dexscript.transpile.skeleton.OutClass;
import com.dexscript.transpile.skeleton.OutField;
import com.dexscript.type.*;

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
        List<DexExpr> iArgs = iNewExpr.posArgs();
        for (DexExpr iArg : iArgs) {
            Translate.$(oClass, iArg);
        }

        String funcName = iNewExpr.target().asRef().toString();
        TypeSystem ts = oClass.typeSystem();

        DexInvocation dexIvc = iNewExpr.invocation();
        Invocation ivc = InferInvocation.$(ts, dexIvc);
        Dispatched dispatched = ts.dispatch(ivc);
        if (dispatched.candidates.isEmpty()) {
            ON_FUNCTION_MISSING.handle(iNewExpr);
        }
        String newF = oClass.oShim().dispatch(funcName, ivc.argsCount(), dispatched);

        OutField oActorField = oClass.allocateField(funcName);
        Gen g = oClass.g();
        g.__(oActorField.value()
        ).__(" = "
        ).__(newF
        ).__("(scheduler, \""
        ).__(funcName
        ).__("\"");
        for (int i = 0; i < iArgs.size(); i++) {
            g.__(", ");
            g.__(Translate.translateExpr(oClass, iArgs.get(i), dispatched.args.get(i)));
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
        iNewExpr.attach(oActorField);
    }
}