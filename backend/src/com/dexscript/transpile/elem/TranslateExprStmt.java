package com.dexscript.transpile.elem;

import com.dexscript.ast.func.DexExprStmt;
import com.dexscript.transpile.OutClass;

public class TranslateExprStmt implements Translate<DexExprStmt> {

    @Override
    public void handle(OutClass oClass, DexExprStmt iElemStmt) {
        Translate.$(oClass, iElemStmt.expr());
    }
}
