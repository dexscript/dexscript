package com.dexscript.transpile.body;

import com.dexscript.ast.expr.DexExpr;
import com.dexscript.ast.stmt.DexReturnStmt;
import com.dexscript.transpile.skeleton.OutClass;
import com.dexscript.transpile.gen.Line;
import com.dexscript.transpile.skeleton.OutDiscardMethod;

public class TranslateReturn implements Translate<DexReturnStmt> {

    @Override
    public void handle(OutClass oClass, DexReturnStmt iReturnStmt) {
        DexExpr iExpr = iReturnStmt.expr();
        Translate.$(oClass, iExpr);
        oClass.g().__("produce("
        ).__(OutValue.of(iExpr)
        ).__(new Line(");")
        ).__(new Line("return;"));
        // discard following statements to avoid javac compiler complaining "unreachable"
        new OutDiscardMethod(oClass);
    }
}
