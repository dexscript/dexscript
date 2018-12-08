package com.dexscript.transpile.body;

import com.dexscript.ast.stmt.DexBlock;
import com.dexscript.ast.stmt.DexStatement;
import com.dexscript.transpile.skeleton.OutClass;

public class TranslateBlock implements Translate<DexBlock> {
    @Override
    public void handle(OutClass oClass, DexBlock iBlk) {
        for (DexStatement stmt : iBlk.stmts()) {
            Translate.$(oClass, stmt);
        }
    }
}
