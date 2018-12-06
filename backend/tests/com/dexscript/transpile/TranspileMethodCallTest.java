package com.dexscript.transpile;

import org.junit.Assert;
import org.junit.Test;

public class TranspileMethodCallTest {

    @Test
    public void method_call_is_same_as_function_call() {
        Object result = Transpile.$("" +
                "function Hello(): string {\n" +
                "   return 'hello world'.World()\n" +
                "}\n" +
                "function World(msg: string): string {\n" +
                "   return msg\n" +
                "}");
        Assert.assertEquals("hello world", result);
    }
}
