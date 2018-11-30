package com.dexscript.ast.expr;

import org.junit.Assert;
import org.junit.Test;

public class DexConsumeExprTest {

    @Test
    public void matched() {
        DexConsumeExpr expr = (DexConsumeExpr) DexExpr.parse("<-actor");
        Assert.assertEquals("<-actor", expr.toString());
        Assert.assertEquals("actor", expr.right().toString());
    }
}
