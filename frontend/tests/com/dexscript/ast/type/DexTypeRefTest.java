package com.dexscript.ast.type;

import org.junit.Assert;
import org.junit.Test;

public class DexTypeRefTest {

    @Test
    public void matched() {
        Assert.assertEquals("hello", new DexTypeRef("hello").toString());
        Assert.assertEquals("hello", new DexTypeRef(" hello ").toString());
        Assert.assertEquals("hello", new DexTypeRef("\thello\t").toString());
        Assert.assertEquals("hello", new DexTypeRef("\rhello\r").toString());
        Assert.assertEquals("hello", new DexTypeRef("\nhello\n").toString());
        Assert.assertEquals("hello", new DexTypeRef(" hello()").toString());
    }
}
