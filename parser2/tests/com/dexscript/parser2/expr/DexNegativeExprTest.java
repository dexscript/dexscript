package com.dexscript.parser2.expr;

import com.dexscript.parser2.expr.DexNegativeExpr;
import org.junit.Assert;
import org.junit.Test;

public class DexNegativeExprTest {

    @Test
    public void matched() {
        Assert.assertEquals("-a", new DexNegativeExpr("-a").toString());
    }
}
