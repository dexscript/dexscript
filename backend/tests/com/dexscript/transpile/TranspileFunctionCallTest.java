package com.dexscript.transpile;

import com.dexscript.runtime.Result;
import org.junit.Assert;
import org.junit.Test;

public class TranspileFunctionCallTest {

    @Test
    public void match_one() {
        Result result = Transpile.$("" +
                "function Hello(): string {\n" +
                "   return World()\n" +
                "}\n" +
                "function World(): string {\n" +
                "   return 'hello world'\n" +
                "}");
        Assert.assertEquals("hello world", result.value());
    }

    @Test
    public void function_with_argument() {
        Result result = Transpile.$("" +
                "function Hello(): string {\n" +
                "   return World('hello world')\n" +
                "}\n" +
                "function World(msg: string): string {\n" +
                "   return msg\n" +
                "}");
        Assert.assertEquals("hello world", result.value());
    }
}
