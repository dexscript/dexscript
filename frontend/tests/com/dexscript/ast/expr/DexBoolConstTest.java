package com.dexscript.ast.expr;

import org.junit.Assert;
import org.junit.Test;

public class DexBoolConstTest {

    @Test
    public void matched() {
        Assert.assertEquals("true", new DexBoolConst("true").toString());
        Assert.assertEquals("false", new DexBoolConst("false").toString());
        Assert.assertEquals("true", new DexBoolConst("true abc").toString());
        Assert.assertEquals("true", new DexBoolConst("true\nabc").toString());
        Assert.assertEquals("true", new DexBoolConst("  true  ").toString());
        Assert.assertEquals("true", new DexBoolConst("  true+").toString());
    }

    @Test
    public void not_matched() {
        Assert.assertFalse(new DexBoolConst("trueabc").matched());
        Assert.assertFalse(new DexBoolConst("abctrue").matched());
        Assert.assertFalse(new DexBoolConst("abc true").matched());
    }
}
