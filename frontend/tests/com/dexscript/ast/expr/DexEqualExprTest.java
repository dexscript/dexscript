package com.dexscript.ast.expr;

import org.junit.Assert;
import org.junit.Test;

public class DexEqualExprTest {

    @Test
    public void matched() {
        Assert.assertEquals("a==b", DexExpr.parse("a==b").toString());
    }
}
