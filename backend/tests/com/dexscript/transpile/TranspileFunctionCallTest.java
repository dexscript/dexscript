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

    @Test
    public void invoke_int64() {
        Object result = Transpile.$("" +
                "function Hello(): int64 {\n" +
                "   return World(100)\n" +
                "}\n" +
                "function World(arg: int64): int64 {" +
                "   return arg\n" +
                "}");
        Assert.assertEquals(100L, result);
    }

    @Test
    public void invoke_int32() {
        Object result = Transpile.$("" +
                "function Hello(): int32 {\n" +
                "   return World(100)\n" +
                "}\n" +
                "function World(arg: int32): int32 {" +
                "   return arg\n" +
                "}");
        Assert.assertEquals(100, result);
    }
}
