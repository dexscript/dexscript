package com.dexscript.transpile.body;

import com.dexscript.ast.expr.DexExpr;
import com.dexscript.ast.expr.DexIndexExpr;
import com.dexscript.ast.expr.DexInvocation;
import com.dexscript.ast.expr.DexValueRef;
import com.dexscript.ast.stmt.DexAssignStmt;
import com.dexscript.infer.InferValue;
import com.dexscript.infer.Value;
import com.dexscript.gen.Line;
import com.dexscript.transpile.skeleton.OutClass;

import java.util.ArrayList;
import java.util.List;

public class TranslateAssign implements Translate<DexAssignStmt> {
    @Override
    public void handle(OutClass oClass, DexAssignStmt iAssignStmt) {
        if (iAssignStmt.targets().size() != 1) {
            throw new UnsupportedOperationException("not implemented");
        }
        DexExpr target = iAssignStmt.targets().get(0);
        if (target instanceof DexValueRef) {
            handle(oClass, (DexValueRef) target, iAssignStmt.expr());
        } else if (target instanceof DexIndexExpr) {
            handle(oClass, (DexIndexExpr) target, iAssignStmt.expr());
        } else {
            throw new IllegalArgumentException();
        }
    }

    private void handle(OutClass oClass, DexValueRef target, DexExpr valExpr) {
        Value ref = InferValue.$(oClass.typeSystem(), target);
        String val = Translate.translateExpr(oClass, valExpr, ref.type());
        oClass.g().__(OutValue.of(ref.definedBy())
        ).__(" = "
        ).__(val
        ).__(new Line(";"));
    }

    private void handle(OutClass oClass, DexIndexExpr target, DexExpr valExpr) {
        List<DexExpr> args = new ArrayList<>();
        args.add(target.obj());
        args.addAll(target.args());
        args.add(valExpr);
        DexInvocation ivc = new DexInvocation(target.pkg(), "Set__", args);
        TranslateInvocation.invoke(oClass, ivc);
    }
}
