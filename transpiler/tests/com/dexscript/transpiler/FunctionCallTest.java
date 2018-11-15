package com.dexscript.transpiler;

import org.junit.Assert;
import org.junit.Test;

public class FunctionCallTest extends TranspilerTest {

    @Test
    public void testFunctionCall() {
        String src = "" +
                "function Hello(): string {\n" +
                "   return World()\n" +
                "}\n" +
                "function World(): string {\n" +
                "   return 'hello'\n" +
                "}\n";
        Assert.assertEquals("hello", transpile1(src));
    }
}
