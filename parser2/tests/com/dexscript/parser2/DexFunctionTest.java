package com.dexscript.parser2;

import org.junit.Assert;
import org.junit.Test;

public class DexFunctionTest {

    @Test
    public void empty() {
        String src = "" +
                "function hello() {\n" +
                "}\n";
        Assert.assertEquals("hello", new DexFunction(src).identifier().toString());
    }
}
