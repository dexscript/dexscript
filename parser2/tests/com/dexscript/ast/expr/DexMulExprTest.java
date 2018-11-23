package com.dexscript.ast.expr;

import org.junit.Assert;
import org.junit.Test;

public class DexMulExprTest {

    @Test
    public void matched() {
        DexMulExpr expr = (DexMulExpr) DexExpr.parse("a*b");
        Assert.assertEquals("a", expr.left().toString());
        Assert.assertEquals("b", expr.right().toString());
        Assert.assertEquals("a*b", expr.toString());
    }
}
