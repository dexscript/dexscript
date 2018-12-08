package com.dexscript.transpile.body;

import com.dexscript.ast.expr.DexValueRef;
import com.dexscript.ast.func.DexAssignStmt;
import com.dexscript.infer.InferValue;
import com.dexscript.infer.Value;
import com.dexscript.transpile.gen.Line;
import com.dexscript.transpile.skeleton.OutClass;

public class TranslateAssign implements Translate<DexAssignStmt> {
    @Override
    public void handle(OutClass oClass, DexAssignStmt iAssignStmt) {
        if (iAssignStmt.targets().size() != 1) {
            throw new UnsupportedOperationException("not implemented");
        }
        Translate.$(oClass, iAssignStmt.expr());
        DexValueRef target = iAssignStmt.targets().get(0);
        Value ref = InferValue.$(oClass.typeSystem(), target);
        oClass.g().__(OutValue.of(ref.definedBy())
        ).__(" = "
        ).__(OutValue.of(iAssignStmt.expr())
        ).__(new Line(";"));
    }
}
