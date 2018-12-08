package com.dexscript.transpile.body;

import com.dexscript.ast.expr.DexEqualExpr;
import com.dexscript.ast.expr.DexExpr;
import com.dexscript.transpile.skeleton.OutClass;

import java.util.ArrayList;
import java.util.List;

public class TranslateEqual implements Translate<DexEqualExpr> {
    @Override
    public void handle(OutClass oClass, DexEqualExpr iEqualExpr) {
        List<DexExpr> args = new ArrayList<>();
        args.add(iEqualExpr.left());
        args.add(iEqualExpr.right());
        TranslateFunctionCall.handle(oClass, iEqualExpr, "Equal__", args);
    }
}
