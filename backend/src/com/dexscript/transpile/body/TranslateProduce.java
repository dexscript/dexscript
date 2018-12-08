package com.dexscript.transpile.body;

import com.dexscript.ast.func.DexProduceStmt;
import com.dexscript.transpile.skeleton.OutClass;

public class TranslateProduce implements Translate<DexProduceStmt> {
    @Override
    public void handle(OutClass oClass, DexProduceStmt iProduceStmt) {
        Translate.$(oClass, iProduceStmt.produced());
        Translate.$(oClass, iProduceStmt.target());
    }
}
