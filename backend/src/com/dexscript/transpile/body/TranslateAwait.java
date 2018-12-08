package com.dexscript.transpile.body;

import com.dexscript.ast.stmt.DexAwaitCase;
import com.dexscript.ast.stmt.DexAwaitStmt;
import com.dexscript.transpile.skeleton.OutClass;

public class TranslateAwait implements Translate<DexAwaitStmt> {

    @Override
    public void handle(OutClass oClass, DexAwaitStmt iAwait) {
        for (DexAwaitCase awaitCase : iAwait.cases()) {
            Translate.$(oClass, awaitCase);
        }
    }
}
