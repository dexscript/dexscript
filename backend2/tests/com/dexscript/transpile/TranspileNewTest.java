package com.dexscript.transpile;

import com.dexscript.runtime.Result;
import org.junit.Assert;
import org.junit.Test;

public class TranspileNewTest {

    @Test
    public void consume_new() {
        Result result = Transpile.$("" +
                "function Hello(): string {\n" +
                "   return <-World{}\n" +
                "}\n" +
                "function World(): string {\n" +
                "   return 'hello world'\n" +
                "}");
        Assert.assertEquals("hello world", result.value());
    }
}
