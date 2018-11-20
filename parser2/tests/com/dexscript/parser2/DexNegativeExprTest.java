package com.dexscript.parser2;

import org.junit.Assert;
import org.junit.Test;

public class DexNegativeExprTest {

    @Test
    public void matched() {
        Assert.assertEquals("-a", new DexNegativeExpr("-a").toString());
    }
}
