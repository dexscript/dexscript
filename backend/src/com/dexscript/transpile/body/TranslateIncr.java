package com.dexscript.transpile.body;

import com.dexscript.ast.expr.DexInvocation;
import com.dexscript.ast.stmt.DexIncrStmt;
import com.dexscript.infer.InferValue;
import com.dexscript.infer.Value;
import com.dexscript.transpile.gen.Line;
import com.dexscript.transpile.skeleton.OutClass;
import com.dexscript.transpile.skeleton.OutField;

public class TranslateIncr implements Translate<DexIncrStmt> {

    @Override
    public void handle(OutClass oClass, DexIncrStmt iIncrStmt) {
        DexInvocation invocation = iIncrStmt.invocation();
        OutField oResultField = TranslateInvocation.invoke(
                oClass, invocation.funcName(), null, invocation.posArgs());
        Value ref = InferValue.$(oClass.typeSystem(), iIncrStmt.target());
        oClass.g().__(OutValue.of(ref.definedBy())
        ).__(" = "
        ).__(oResultField.value()
        ).__(new Line(";"));
    }
}
