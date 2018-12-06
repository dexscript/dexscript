package com.dexscript.transpile.elem;

import com.dexscript.ast.core.DexElement;
import com.dexscript.ast.expr.DexExpr;
import com.dexscript.ast.expr.DexMethodCallExpr;
import com.dexscript.transpile.OutClass;

import java.util.ArrayList;
import java.util.List;

public class TranslateMethodCall implements TranslateElem {

    @Override
    public void handle(OutClass oClass, DexElement iElem) {
        DexMethodCallExpr iCallExpr = (DexMethodCallExpr) iElem;
        String funcName = iCallExpr.method().toString();
        List<DexExpr> args = new ArrayList<>();
        args.add(iCallExpr.obj());
        args.addAll(iCallExpr.args());
        TranslateFunctionCall.handle(oClass, (DexExpr) iElem, funcName, args);
    }
}
