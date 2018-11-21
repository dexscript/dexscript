package com.dexscript.parser2;

import org.junit.Assert;
import org.junit.Test;

public class DexCallExprTest {

    @Test
    public void matched() {
        DexCallExpr call = (DexCallExpr) DexExpr.parse("print(a,b,c)");
        Assert.assertEquals("print", call.target().toString());
        Assert.assertEquals(3, call.args().size());
        Assert.assertEquals("a", call.args().get(0).toString());
        Assert.assertEquals("b", call.args().get(1).toString());
        Assert.assertEquals("c", call.args().get(2).toString());
    }
}
