package com.dexscript.transpile.body;

import com.dexscript.ast.expr.DexValueRef;
import com.dexscript.ast.stmt.DexAssignStmt;
import com.dexscript.infer.InferType;
import com.dexscript.infer.InferValue;
import com.dexscript.infer.Value;
import com.dexscript.transpile.gen.Line;
import com.dexscript.transpile.skeleton.OutClass;
import com.dexscript.type.*;

public class TranslateAssign implements Translate<DexAssignStmt> {
    @Override
    public void handle(OutClass oClass, DexAssignStmt iAssignStmt) {
        if (iAssignStmt.targets().size() != 1) {
            throw new UnsupportedOperationException("not implemented");
        }
        Translate.$(oClass, iAssignStmt.expr());
        TypeSystem ts = oClass.typeSystem();
        if (InferType.$(ts, iAssignStmt.expr()) instanceof IntegerConstType) {
            handleIntegerConst(oClass, iAssignStmt);
            return;
        }
        DexValueRef target = iAssignStmt.targets().get(0);
        Value ref = InferValue.$(ts, target);
        oClass.g().__(OutValue.of(ref.definedBy())
        ).__(" = "
        ).__(OutValue.of(iAssignStmt.expr())
        ).__(new Line(";"));
    }

    private void handleIntegerConst(OutClass oClass, DexAssignStmt iAssignStmt) {
        DexValueRef target = iAssignStmt.targets().get(0);
        Value ref = InferValue.$(oClass.typeSystem(), target);
        String val = OutValue.of(iAssignStmt.expr());
        if (ref.type() instanceof Int32Type) {
            val = "Integer.valueOf((int)" + val + ")";
        } else if (ref.type() instanceof Int64Type || ref.type() instanceof IntegerLiteralType) {
            // keep it as Long
        }
        oClass.g().__(OutValue.of(ref.definedBy())
        ).__(" = "
        ).__(val
        ).__(new Line(";"));
    }
}
