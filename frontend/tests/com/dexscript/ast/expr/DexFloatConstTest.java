package com.dexscript.ast.expr;

import org.junit.Assert;
import org.junit.Test;

public class DexFloatConstTest {

    @Test
    public void matched() {
        Assert.assertEquals("0", new DexFloatConst("0").toString());
        Assert.assertEquals("0.1", new DexFloatConst("0.1").toString());
        Assert.assertEquals("100", new DexFloatConst("100").toString());
        Assert.assertEquals("1.1", new DexFloatConst("1.1").toString());
        Assert.assertEquals("1.1", new DexFloatConst("1.1.1").toString());
        Assert.assertEquals("1e100", new DexFloatConst("1e100").toString());
        Assert.assertEquals("3.14e-10", new DexFloatConst("3.14e-10").toString());
    }

    @Test
    public void with_error() {
        Assert.assertEquals("3.14e-<error/>", new DexFloatConst("3.14e-").toString());
    }
}
