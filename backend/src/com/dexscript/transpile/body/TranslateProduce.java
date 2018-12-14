package com.dexscript.transpile.body;

import com.dexscript.ast.stmt.DexProduceStmt;
import com.dexscript.transpile.gen.Line;
import com.dexscript.transpile.skeleton.OutClass;

public class TranslateProduce implements Translate<DexProduceStmt> {
    @Override
    public void handle(OutClass oClass, DexProduceStmt iProduceStmt) {
        String produced = "null";
        if (iProduceStmt.produced() != null) {
            Translate.$(oClass, iProduceStmt.produced());
            produced = OutValue.of(iProduceStmt.produced());
        }
        Translate.$(oClass, iProduceStmt.target());
        String target = OutValue.of(iProduceStmt.target());
        oClass.g().__("((Actor)"
        ).__(target
        ).__(").produce("
        ).__(produced
        ).__(new Line(");"));
    }
}
