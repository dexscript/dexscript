package com.dexscript.ast.expr;

import org.junit.Assert;
import org.junit.Test;

public class DexValueRefTest {

    @Test
    public void matched() {
        Assert.assertEquals("hello", new DexValueRef("hello").toString());
        Assert.assertEquals("hello", new DexValueRef(" hello ").toString());
        Assert.assertEquals("hello", new DexValueRef("\thello\t").toString());
        Assert.assertEquals("hello", new DexValueRef("\rhello\r").toString());
        Assert.assertEquals("hello", new DexValueRef("\nhello\n").toString());
        Assert.assertEquals("hello", new DexValueRef(" hello()").toString());
    }
}
