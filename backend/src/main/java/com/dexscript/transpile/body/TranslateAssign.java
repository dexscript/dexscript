package com.dexscript.transpile.body;

import com.dexscript.ast.expr.DexValueRef;
import com.dexscript.ast.stmt.DexAssignStmt;
import com.dexscript.infer.InferValue;
import com.dexscript.infer.Value;
import com.dexscript.gen.Line;
import com.dexscript.transpile.skeleton.OutClass;

public class TranslateAssign implements Translate<DexAssignStmt> {
    @Override
    public void handle(OutClass oClass, DexAssignStmt iAssignStmt) {
        if (iAssignStmt.targets().size() != 1) {
            throw new UnsupportedOperationException("not implemented");
        }
        DexValueRef target = (DexValueRef) iAssignStmt.targets().get(0);
        Value ref = InferValue.$(oClass.typeSystem(), target);
        String val = Translate.translateExpr(oClass, iAssignStmt.expr(), ref.type());
        oClass.g().__(OutValue.of(ref.definedBy())
        ).__(" = "
        ).__(val
        ).__(new Line(";"));
    }
}
