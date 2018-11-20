package com.dexscript.parser2;

import org.junit.Assert;
import org.junit.Test;

public class DexExprTest {

    @Test
    public void unary_then_binary() {
        DexAddExpr expr = (DexAddExpr) DexExpr.parse("+a+b");
        Assert.assertEquals("+a", expr.left().toString());
        Assert.assertEquals("b", expr.right().toString());
    }
}
