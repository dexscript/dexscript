package com.dexscript.transpile.body;

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
        String funcName = iNewExpr.target().asRef().toString();
        TypeSystem ts = oClass.typeSystem();

        DexInvocation dexIvc = iNewExpr.invocation();
        Invocation ivc = InferInvocation.ivc(ts, dexIvc);
        Dispatched dispatched = ts.dispatch(ivc);
        if (dispatched.candidates.isEmpty()) {
            System.out.println("can not find candidates for: " + ivc);
            dispatched.dump();
            throw new DexRuntimeException("missing implementation");
        }
        List<String> translatedArgs = Translate.translateArgs(oClass, dexIvc, dispatched);
        String newF = oClass.oShim().dispatch(funcName, ivc.argsCount(), dispatched);

        OutField oActorField = oClass.allocateField(funcName);
        Gen g = oClass.g();
        g.__(oActorField.value()
        ).__(" = "
        ).__(newF
        ).__("(scheduler");
        for (String translatedArg : translatedArgs) {
            g.__(", ");
            g.__(translatedArg);
        }
        g.__(new Line(");"));
        iNewExpr.attach(oActorField);
    }
}
