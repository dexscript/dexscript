package com.dexscript.parser2;

import org.junit.Assert;
import org.junit.Test;

public class DexAddExprTest {

    @Test
    public void matched() {
        DexAddExpr expr = (DexAddExpr) DexExpr.parse("a+b");
        Assert.assertEquals("a", expr.left().toString());
        Assert.assertEquals("b", expr.right().toString());
        Assert.assertEquals("a+b", expr.toString());
    }
}
