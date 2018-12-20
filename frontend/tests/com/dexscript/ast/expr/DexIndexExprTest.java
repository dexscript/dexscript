package com.dexscript.ast.expr;

import org.junit.Assert;
import org.junit.Test;

public class DexIndexExprTest {

    @Test
    public void matched() {
        DexIndexExpr expr = (DexIndexExpr) DexExpr.parse("a[b]");
        Assert.assertEquals("a[b]", expr.toString());
        Assert.assertEquals(1, expr.args().size());
        DexInvocation invocation = expr.invocation();
        Assert.assertEquals("a", invocation.args().get(0).toString());
    }
}
