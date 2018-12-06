package com.dexscript.transpile.body;

import com.dexscript.ast.expr.DexExpr;
import com.dexscript.ast.expr.DexNewExpr;
import com.dexscript.infer.InferType;
import com.dexscript.transpile.skeleton.OutClass;
import com.dexscript.transpile.skeleton.OutField;
import com.dexscript.transpile.gen.Gen;
import com.dexscript.transpile.gen.Line;
import com.dexscript.type.FunctionType;
import com.dexscript.type.Type;
import com.dexscript.type.TypeSystem;

import java.util.List;

public class TranslateNew implements Translate<DexNewExpr> {

    @Override
    public void handle(OutClass oClass, DexNewExpr iNewExpr) {
        List<DexExpr> iArgs = iNewExpr.args();
        for (DexExpr iArg : iArgs) {
            Translate.$(oClass, iArg);
        }

        String funcName = iNewExpr.target().asRef().toString();
        TypeSystem ts = oClass.typeSystem();

        Type actorType = InferType.$(ts, iNewExpr);

        List<FunctionType> funcTypes = ts.resolveFunctions(funcName, InferType.inferTypes(ts, iArgs));
        String newF = oClass.oShim().combineNewF(funcName, iArgs.size(), funcTypes);
        OutField oActorField = oClass.allocateField(funcName, actorType);
        Gen g = oClass.g();
        g.__(oActorField.value()
        ).__(" = "
        ).__(newF
        ).__('(');
        for (int i = 0; i < iArgs.size(); i++) {
            if (i > 0) {
                g.__(", ");
            }
            DexExpr iArg = iArgs.get(i);
            OutValue oValue = iArg.attachmentOfType(OutValue.class);
            g.__(oValue.value());
        }
        g.__(new Line(");"));
        iNewExpr.attach(oActorField);
    }
}
