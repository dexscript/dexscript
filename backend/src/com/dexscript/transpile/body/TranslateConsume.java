package com.dexscript.transpile.body;

import com.dexscript.ast.expr.DexConsumeExpr;
import com.dexscript.transpile.skeleton.OutClass;

public class TranslateConsume implements Translate<DexConsumeExpr> {

    @Override
    public void handle(OutClass oClass, DexConsumeExpr iConsumeExpr) {
        Translate.$(oClass, iConsumeExpr.right());
        String targetActor = iConsumeExpr.right().attachmentOfType(OutValue.class).value();
        TranslateFunctionCall.consume(oClass, iConsumeExpr, targetActor);
    }
}
