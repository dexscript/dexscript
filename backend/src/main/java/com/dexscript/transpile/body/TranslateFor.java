package com.dexscript.transpile.body;

import com.dexscript.ast.stmt.DexForStmt;
import com.dexscript.transpile.skeleton.OutClass;

public class TranslateFor implements Translate<DexForStmt> {

    private final TranslateForWith3Clauses translateForWith3Clauses = new TranslateForWith3Clauses();

    @Override
    public void handle(OutClass oClass, DexForStmt iForStmt) {
        if (iForStmt.isForWith3Clauses()) {
            translateForWith3Clauses.handle(oClass, iForStmt);
        }
    }
}
