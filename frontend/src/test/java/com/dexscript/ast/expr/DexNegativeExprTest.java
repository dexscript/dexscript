package com.dexscript.ast.expr;

import org.junit.Assert;
import org.junit.Test;

public class DexNegativeExprTest {

    @Test
    public void matched() {
        Assert.assertEquals("-a", new DexNegativeExpr("-a").toString());
    }
}
