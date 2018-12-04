package com.dexscript.transpile.elem;

import com.dexscript.ast.core.DexElement;
import com.dexscript.ast.expr.DexExpr;
import com.dexscript.ast.func.DexReturnStmt;
import com.dexscript.transpile.OutClass;
import com.dexscript.transpile.OutValue;
import com.dexscript.transpile.gen.Line;
import com.dexscript.type.Type;
import com.dexscript.type.TypeSystem;

public class TranslateReturnStmt implements Translate {

    @Override
    public void handle(OutClass oClass, DexElement iElem) {
        DexReturnStmt iReturnStmt = (DexReturnStmt) iElem;
        TypeSystem ts = oClass.typeSystem();
        Type retType = ts.resolveType(oClass.iFunc().sig().ret());
        DexExpr iExpr = iReturnStmt.expr();
        Translate.$(oClass, iExpr);
        oClass.g().__("finish(("
        ).__(retType.javaClassName()
        ).__(')'
        ).__(iExpr.attachmentOfType(OutValue.class).value()
        ).__(new Line(");"));
    }
}
