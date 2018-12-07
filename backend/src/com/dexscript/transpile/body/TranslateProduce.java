package com.dexscript.transpile.body;

import com.dexscript.ast.expr.DexProduceExpr;
import com.dexscript.transpile.skeleton.OutClass;

public class TranslateProduce implements Translate<DexProduceExpr> {
    @Override
    public void handle(OutClass oClass, DexProduceExpr iProduceExpr) {
        Translate.$(oClass, iProduceExpr.left());
        Translate.$(oClass, iProduceExpr.right());
    }
}
