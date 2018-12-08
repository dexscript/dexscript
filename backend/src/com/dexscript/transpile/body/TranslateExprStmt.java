package com.dexscript.transpile.body;

import com.dexscript.ast.stmt.DexExprStmt;
import com.dexscript.transpile.skeleton.OutClass;

public class TranslateExprStmt implements Translate<DexExprStmt> {

    @Override
    public void handle(OutClass oClass, DexExprStmt iElemStmt) {
        Translate.$(oClass, iElemStmt.expr());
    }
}
