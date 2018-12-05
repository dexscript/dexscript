package com.dexscript.transpile;

import org.junit.Assert;
import org.junit.Test;

public class TranspileNewTest {

    @Test
    public void consume_new() {
        Object result = Transpile.$("" +
                "function Hello(): string {\n" +
                "   return <-World{}\n" +
                "}\n" +
                "function World(): string {\n" +
                "   return 'hello world'\n" +
                "}");
        Assert.assertEquals("hello world", result);
    }
}
