package com.dexscript.ast.expr;

import org.junit.Assert;
import org.junit.Test;

public class DexConsumeExprTest {

    @Test
    public void matched() {
        DexConsumeExpr expr = (DexConsumeExpr) DexExpr.$parse("<-actor");
        Assert.assertEquals("<-actor", expr.toString());
        Assert.assertEquals("actor", expr.right().toString());
    }

    @Test
    public void consume_new_actor() {
        DexConsumeExpr expr = (DexConsumeExpr) DexExpr.$parse("<-Hello{100}");
        Assert.assertEquals("<-Hello{100}", expr.toString());
        Assert.assertEquals("Hello{100}", expr.right().toString());
    }
}
