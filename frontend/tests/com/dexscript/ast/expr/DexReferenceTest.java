package com.dexscript.ast.expr;

import org.junit.Assert;
import org.junit.Test;

public class DexReferenceTest {

    @Test
    public void matched() {
        Assert.assertEquals("hello", new DexReference("hello").toString());
        Assert.assertEquals("hello", new DexReference(" hello ").toString());
        Assert.assertEquals("hello", new DexReference("\thello\t").toString());
        Assert.assertEquals("hello", new DexReference("\rhello\r").toString());
        Assert.assertEquals("hello", new DexReference("\nhello\n").toString());
        Assert.assertEquals("hello", new DexReference(" hello()").toString());
    }
}
