package com.dexscript.transpile.body;

import com.dexscript.ast.expr.DexConsumeExpr;
import com.dexscript.infer.InferType;
import com.dexscript.transpile.skeleton.OutClass;
import com.dexscript.transpile.skeleton.OutField;
import com.dexscript.type.core.DType;

public class TranslateConsume implements Translate<DexConsumeExpr> {

    @Override
    public void handle(OutClass oClass, DexConsumeExpr iConsumeExpr) {
        Translate.$(oClass, iConsumeExpr.right());
        String targetActor = iConsumeExpr.right().attachmentOfType(OutValue.class).value();
        DType retType = InferType.$(oClass.typeSystem(), iConsumeExpr);
        OutField oResultField = TranslateInvocation.consume(oClass, retType, targetActor);
        if (oResultField != null) {
            iConsumeExpr.attach(oResultField);
        }
    }
}
