package com.dexscript.ast.expr;

import org.junit.Assert;
import org.junit.Test;

public class DexProduceExprTest {

    @Test
    public void matched() {
        DexProduceExpr produceExpr = (DexProduceExpr) DexExpr.parse("'hello' -> abc");
        Assert.assertEquals("'hello'", produceExpr.left().toString());
        Assert.assertEquals("abc", produceExpr.right().toString());
    }
}
