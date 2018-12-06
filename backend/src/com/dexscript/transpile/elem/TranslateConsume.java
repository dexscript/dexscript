package com.dexscript.transpile.elem;

import com.dexscript.ast.expr.DexConsumeExpr;
import com.dexscript.transpile.OutClass;
import com.dexscript.transpile.OutValue;

public class TranslateConsume implements Translate<DexConsumeExpr> {

    @Override
    public void handle(OutClass oClass, DexConsumeExpr iConsumeExpr) {
        Translate.$(oClass, iConsumeExpr.right());
        OutValue outValue = iConsumeExpr.right().attachmentOfType(OutValue.class);
        iConsumeExpr.attach(new OutValue(outValue.value() + ".value()"));
    }
}
