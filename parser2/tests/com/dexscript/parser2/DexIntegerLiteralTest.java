package com.dexscript.parser2;

import org.junit.Assert;
import org.junit.Test;

public class DexIntegerLiteralTest {

    @Test
    public void matched() {
        Assert.assertEquals("1", new DexIntegerLiteral("1").toString());
        Assert.assertEquals("0", new DexIntegerLiteral("01").toString());
        Assert.assertEquals("100", new DexIntegerLiteral("100").toString());
        Assert.assertEquals("100", new DexIntegerLiteral("100a").toString());
    }

    @Test
    public void unmatched() {
        Assert.assertEquals("<unmatched>a</unmatched>", new DexIntegerLiteral("a").toString());
    }
}
