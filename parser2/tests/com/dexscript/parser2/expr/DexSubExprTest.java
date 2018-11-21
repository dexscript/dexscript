package com.dexscript.parser2.expr;

import com.dexscript.parser2.expr.DexExpr;
import com.dexscript.parser2.expr.DexSubExpr;
import org.junit.Assert;
import org.junit.Test;

public class DexSubExprTest {

    @Test
    public void matched() {
        DexSubExpr expr = (DexSubExpr) DexExpr.parse("a-b");
        Assert.assertEquals("a", expr.left().toString());
        Assert.assertEquals("b", expr.right().toString());
        Assert.assertEquals("a-b", expr.toString());
    }

    @Test
    public void with_space() {
        DexSubExpr expr = (DexSubExpr) DexExpr.parse("a - b");
        Assert.assertEquals("a", expr.left().toString());
        Assert.assertEquals("b", expr.right().toString());
        Assert.assertEquals("a - b", expr.toString());
    }
}
