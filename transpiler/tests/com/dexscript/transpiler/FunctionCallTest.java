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
    public void test_call_with_one_argument() {
        String src = "" +
                "function Hello(): string {\n" +
                "   return World('hello')\n" +
                "}\n" +
                "function World(msg: string): string {\n" +
                "   return msg\n" +
                "}\n";
        Assert.assertEquals("hello", transpile1(src));
    }

    @Test
    public void test_call_with_two_argument() {
        String src = "" +
                "function Hello(): string {\n" +
                "   return World('hello', 'world')\n" +
                "}\n" +
                "function World(msg1: string, msg2: string): string {\n" +
                "   return msg1 + ' ' + msg2\n" +
                "}\n";
        Assert.assertEquals("hello world", transpile1(src));
    }
}
