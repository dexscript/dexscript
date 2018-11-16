package com.dexscript.transpiler;

import org.junit.Assert;
import org.junit.Test;

public class FunctionCallTest extends TranspilerTest {

    @Test
    public void test_call_no_argument_function() {
        String src = "" +
                "function Hello(): string {\n" +
                "   return World()\n" +
                "}\n" +
                "function World(): string {\n" +
                "   return 'hello'\n" +
                "}\n";
        Assert.assertEquals("hello", transpile1(src));
    }

    @Test
    public void test_call_sleep() {
        String src = "" +
                "function Hello() {\n" +
                "   Sleep(1000)\n" +
                "}";
        transpile0(src);
    }
}
