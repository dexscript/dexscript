package com.dexscript.parser2.expr;

import com.dexscript.parser2.expr.DexExpr;
import com.dexscript.parser2.expr.DexMulExpr;
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