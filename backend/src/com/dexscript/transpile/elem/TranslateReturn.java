package com.dexscript.transpile.elem;

import com.dexscript.ast.core.DexElement;
import com.dexscript.ast.expr.DexExpr;
import com.dexscript.ast.func.DexReturnStmt;
import com.dexscript.transpile.OutClass;
import com.dexscript.transpile.OutValue;
import com.dexscript.transpile.gen.Line;

public class TranslateReturn implements Translate {

    @Override
    public void handle(OutClass oClass, DexElement iElem) {
        DexReturnStmt iReturnStmt = (DexReturnStmt) iElem;
        DexExpr iExpr = iReturnStmt.expr();
        Translate.$(oClass, iExpr);
        oClass.g().__("finish("
        ).__(iExpr.attachmentOfType(OutValue.class).value()
        ).__(new Line(");"));
    }
}
