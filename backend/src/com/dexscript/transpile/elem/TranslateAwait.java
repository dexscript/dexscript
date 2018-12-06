package com.dexscript.transpile.elem;

import com.dexscript.ast.func.DexAwaitCase;
import com.dexscript.ast.func.DexAwaitStmt;
import com.dexscript.transpile.OutClass;

public class TranslateAwait implements Translate<DexAwaitStmt> {

    @Override
    public void handle(OutClass oClass, DexAwaitStmt iAwait) {
        for (DexAwaitCase awaitCase : iAwait.cases()) {
            Translate.$(oClass, awaitCase);
        }
    }
}
