package com.dexscript.ast.expr;

import org.junit.Assert;
import org.junit.Test;

public class DexIntegerConstTest {

    @Test
    public void matched() {
        Assert.assertEquals("1", new DexIntegerConst("1").toString());
        Assert.assertEquals("0", new DexIntegerConst("01").toString());
        Assert.assertEquals("100", new DexIntegerConst("100").toString());
        Assert.assertEquals("100", new DexIntegerConst("100a").toString());
    }

    @Test
    public void unmatched() {
        Assert.assertEquals("<unmatched>a</unmatched>", new DexIntegerConst("a").toString());
    }
}
