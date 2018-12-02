package com.dexscript.ast.expr;

import org.junit.Assert;
import org.junit.Test;

public class DexFloatLiteralTest {

    @Test
    public void matched() {
        Assert.assertEquals("0", new DexFloatLiteral("0").toString());
        Assert.assertEquals("0.1", new DexFloatLiteral("0.1").toString());
        Assert.assertEquals("100", new DexFloatLiteral("100").toString());
        Assert.assertEquals("1.1", new DexFloatLiteral("1.1").toString());
        Assert.assertEquals("1.1", new DexFloatLiteral("1.1.1").toString());
        Assert.assertEquals("1e100", new DexFloatLiteral("1e100").toString());
        Assert.assertEquals("3.14e-10", new DexFloatLiteral("3.14e-10").toString());
    }

    @Test
    public void with_error() {
        Assert.assertEquals("3.14e-<error/>", new DexFloatLiteral("3.14e-").toString());
    }
}
