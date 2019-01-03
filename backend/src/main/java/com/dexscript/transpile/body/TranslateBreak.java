package com.dexscript.transpile.body;

import com.dexscript.ast.stmt.DexBreakStmt;
import com.dexscript.ast.stmt.DexForStmt;
import com.dexscript.gen.Line;
import com.dexscript.transpile.skeleton.OutClass;

public class TranslateBreak implements Translate<DexBreakStmt> {
    @Override
    public void handle(OutClass oClass, DexBreakStmt iBreakStmt) {
        DexForStmt iForStmt = iBreakStmt.enclosingForStmt();
        oClass.g().__(new Line("break;"));
    }
}
