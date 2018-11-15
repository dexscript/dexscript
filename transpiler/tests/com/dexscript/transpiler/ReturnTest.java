package com.dexscript.transpiler;

import org.junit.Assert;
import org.junit.Test;

public class ReturnTest extends TranspilerTest {

    @Test
    public void testReturnVoid() {
        String src = "" +
                "function Hello() {\n" +
                "   return\n" +
                "}\n";
        transpile0(src);
    }

    @Test
    public void testReturnStringLiteral() {
        String src = "" +
                "function Hello(): string {\n" +
                "   return 'hello'\n" +
                "}\n";
        Assert.assertEquals("hello", transpile1(src));
    }

    @Test
    public void testReturnInt64() {
        String src = "" +
                "function Hello(): int64 {\n" +
                "   return 1\n" +
                "}\n";
        Assert.assertEquals((long)1, transpile1(src));
    }

    @Test
    public void testReturnInt32() {
        String src = "" +
                "function Hello(): int32 {\n" +
                "   return (int32)1\n" +
                "}\n";
        Assert.assertEquals(1, transpile1(src));
    }
}
