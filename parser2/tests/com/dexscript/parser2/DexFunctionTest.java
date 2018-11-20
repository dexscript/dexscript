package com.dexscript.parser2;

import org.junit.Assert;
import org.junit.Test;

public class DexFunctionTest {

    @Test
    public void empty() {
        String src = "" +
                " function hello() {\n" +
                "}\n";
        DexFunction function = new DexFunction(src);
        Assert.assertTrue(function.matched());
        Assert.assertEquals("hello", function.identifier().toString());
        Assert.assertEquals(src.substring(1), function.toString());
    }

    @Test
    public void missing_lbrace() {
        String src = "" +
                "function hello ) {\n" +
                "}\n";
        DexFunction function = new DexFunction(src);
        Assert.assertNull(function.identifier());
        Assert.assertEquals(15, function.err().errorBegin);
    }
}
