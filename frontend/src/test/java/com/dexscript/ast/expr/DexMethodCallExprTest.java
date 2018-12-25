package com.dexscript.ast.expr;

import org.junit.Assert;
import org.junit.Test;

public class DexMethodCallExprTest {

    @Test
    public void matched() {
        DexMethodCallExpr methodCallExpr = (DexMethodCallExpr) DexExpr.$parse("a.b()");
        Assert.assertEquals("a.b()", methodCallExpr.toString());
        Assert.assertEquals("a", methodCallExpr.obj().toString());
        Assert.assertEquals("b", methodCallExpr.method().toString());
        Assert.assertEquals(0, methodCallExpr.posArgs().size());
    }
}
