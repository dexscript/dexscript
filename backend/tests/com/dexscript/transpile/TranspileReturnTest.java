package com.dexscript.transpile;

import org.junit.Assert;
import org.junit.Test;

public class TranspileReturnTest {

    @Test
    public void return_string_literal() {
        Object result = Transpile.$("" +
                "function Hello(): string {\n" +
                "   return 'hello'\n" +
                "}");
        Assert.assertEquals("hello", result);
    }
}
