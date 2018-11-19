package com.dexscript.parser2;

import org.junit.Assert;
import org.junit.Test;

public class DexIdentifierTest {

    @Test
    public void hello() {
        Assert.assertEquals("hello", new DexIdentifier("hello").toString());
        Assert.assertEquals("hello", new DexIdentifier(" hello ").toString());
        Assert.assertEquals("hello", new DexIdentifier("\thello\t").toString());
        Assert.assertEquals("hello", new DexIdentifier("\rhello\r").toString());
        Assert.assertEquals("hello", new DexIdentifier("\nhello\n").toString());
        Assert.assertEquals("hello", new DexIdentifier(" hello()").toString());
    }
}
