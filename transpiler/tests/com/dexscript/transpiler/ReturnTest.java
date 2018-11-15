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
}
