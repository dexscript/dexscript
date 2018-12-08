package com.dexscript.transpile.body;

import com.dexscript.ast.func.DexProduceStmt;
import com.dexscript.transpile.gen.Line;
import com.dexscript.transpile.skeleton.OutClass;

public class TranslateProduce implements Translate<DexProduceStmt> {
    @Override
    public void handle(OutClass oClass, DexProduceStmt iProduceStmt) {
        Translate.$(oClass, iProduceStmt.produced());
        Translate.$(oClass, iProduceStmt.target());
        String produced = OutValue.of(iProduceStmt.produced());
        String target = OutValue.of(iProduceStmt.target());
        oClass.g().__(target
        ).__(".finish("
        ).__(produced
        ).__(new Line(");"));
    }
}
