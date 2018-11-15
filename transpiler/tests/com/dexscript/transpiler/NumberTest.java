package com.dexscript.transpiler;

import org.junit.Assert;
import org.junit.Test;

public class NumberTest extends TranspilerTest {

    @Test
    public void test_int64_add_int64() {
        String src = "" +
                "function Hello(): int64 {\n" +
                "   return 1+2\n" +
                "}\n";
        Assert.assertEquals((long) 3, transpile1(src));
    }

    @Test
    public void test_int32_add_int32() {
        String src = "" +
                "function Hello(): int32 {\n" +
                "   return (int32)1+(int32)2\n" +
                "}\n";
        Assert.assertEquals(3, transpile1(src));
    }
}
