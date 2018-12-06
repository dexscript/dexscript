package com.dexscript.transpile;

import org.junit.Assert;
import org.junit.Test;

public class TranspileFunctionCallTest {

    @Test
    public void match_one() {
        Object result = Transpile.$("" +
                "function Hello(): string {\n" +
                "   return World()\n" +
                "}\n" +
                "function World(): string {\n" +
                "   return 'hello world'\n" +
                "}");
        Assert.assertEquals("hello world", result);
    }

    @Test
    public void function_with_argument() {
        Object result = Transpile.$("" +
                "function Hello(): string {\n" +
                "   return World('hello world')\n" +
                "}\n" +
                "function World(msg: string): string {\n" +
                "   return msg\n" +
                "}");
        Assert.assertEquals("hello world", result);
    }

    @Test
    public void function_without_return_value() {
        Object result = Transpile.$("" +
                "function Hello(): string {\n" +
                "   World()\n" +
                "   return 'hello world'\n" +
                "}\n" +
                "function World() {\n" +
                "}");
        Assert.assertEquals("hello world", result);
    }
}
