package com.dexscript.transpiler;

import org.junit.Assert;
import org.junit.Test;

public class NumberTest extends TranspilerTest {

    @Test
    public void testPlus() {
        String src = "" +
                "function Hello(): int64 {\n" +
                "   return 1+2\n" +
                "}\n";
        Assert.assertEquals((long) 3, transpile1(src));
    }
}
