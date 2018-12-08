package com.dexscript.transpile.body;

import com.dexscript.ast.stmt.DexElseStmt;
import com.dexscript.transpile.skeleton.OutClass;

public class TranslateElse implements Translate<DexElseStmt> {

    @Override
    public void handle(OutClass oClass, DexElseStmt iElseStmt) {
        if (iElseStmt.hasIf()) {
            Translate.$(oClass, iElseStmt.ifStmt());
        } else {
            Translate.$(oClass, iElseStmt.blk());
        }
    }
}
