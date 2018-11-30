package com.dexscript.ast.expr;

import org.junit.Assert;
import org.junit.Test;

public class DexGetResultExprTest {

    @Test
    public void matched() {
        DexGetResultExpr expr = (DexGetResultExpr) DexExpr.parse("<-actor");
        Assert.assertEquals("<-actor", expr.toString());
        Assert.assertEquals("actor", expr.right().toString());
    }
}
