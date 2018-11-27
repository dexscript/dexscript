package com.dexscript.ast.func;

import com.dexscript.ast.elem.DexIdentifier;
import org.junit.Assert;
import org.junit.Test;

public class DexIdentifierTest {

    @Test
    public void matched() {
        Assert.assertEquals("hello", new DexIdentifier("hello").toString());
        Assert.assertEquals("hello", new DexIdentifier(" hello ").toString());
        Assert.assertEquals("hello", new DexIdentifier("\thello\t").toString());
        Assert.assertEquals("hello", new DexIdentifier("\rhello\r").toString());
        Assert.assertEquals("hello", new DexIdentifier("\nhello\n").toString());
        Assert.assertEquals("hello", new DexIdentifier(" hello()").toString());
    }

    @Test
    public void unmatched() {
        Assert.assertFalse(new DexIdentifier("+ hello").matched());
        Assert.assertEquals("<unmatched>+ hello</unmatched>", new DexIdentifier("+ hello").toString());
        Assert.assertEquals("<unmatched>hello?</unmatched>", new DexIdentifier("hello?").toString());
        Assert.assertFalse(new DexIdentifier("0").matched());
        Assert.assertFalse(new DexIdentifier("hello+").matched());
    }
}
