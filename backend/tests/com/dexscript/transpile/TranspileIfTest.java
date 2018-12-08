package com.dexscript.transpile;

import org.junit.Assert;
import org.junit.Test;

public class TranspileIfTest {

    @Test
    public void only_if() {
        Object result = Transpile.$("" +
                "function Hello(): string {\n" +
                "   if 1 == 1 {\n" +
                "       return 'hello'\n" +
                "   }\n" +
                "   return 'world'\n" +
                "}");
        Assert.assertEquals("hello", result);
    }
}
