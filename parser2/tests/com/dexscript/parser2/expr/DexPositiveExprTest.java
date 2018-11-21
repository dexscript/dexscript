package com.dexscript.parser2.expr;

import com.dexscript.parser2.expr.DexPositiveExpr;
import org.junit.Assert;
import org.junit.Test;

public class DexPositiveExprTest {

    @Test
    public void matched() {
        Assert.assertEquals("+a", new DexPositiveExpr("+a").toString());
        Assert.assertEquals("+", new DexPositiveExpr("+").toString());
    }

    @Test
    public void unmatched() {
        Assert.assertEquals("<unmatched>-</unmatched>", new DexPositiveExpr("-").toString());
    }
}
