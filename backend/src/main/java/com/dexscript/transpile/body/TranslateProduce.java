package com.dexscript.transpile.body;

import com.dexscript.ast.stmt.DexProduceStmt;
import com.dexscript.gen.Line;
import com.dexscript.transpile.skeleton.OutClass;
import com.dexscript.type.core.*;

public class TranslateProduce implements Translate<DexProduceStmt> {
    @Override
    public void handle(OutClass oClass, DexProduceStmt iProduceStmt) {
        String produced = "null";
        if (iProduceStmt.produced() != null) {
            Translate.$(oClass, iProduceStmt.produced());
            DType retType = InferType.$(oClass.typeSystem(), null, iProduceStmt.sig().ret());
            produced = Translate.translateExpr(oClass, iProduceStmt.produced(), retType);
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
