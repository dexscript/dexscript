package com.dexscript.transpile.body;

import com.dexscript.ast.expr.DexExpr;
import com.dexscript.ast.stmt.DexReturnStmt;
import com.dexscript.transpile.skeleton.OutClass;
import com.dexscript.transpile.gen.Line;

public class TranslateReturn implements Translate<DexReturnStmt> {

    @Override
    public void handle(OutClass oClass, DexReturnStmt iReturnStmt) {
        DexExpr iExpr = iReturnStmt.expr();
        Translate.$(oClass, iExpr);
        oClass.g().__("finish("
        ).__(OutValue.of(iExpr)
        ).__(new Line(");"));
    }
}
