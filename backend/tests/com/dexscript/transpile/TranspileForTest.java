package com.dexscript.transpile;

import org.junit.Assert;
import org.junit.Test;

public class TranspileForTest {

    @Test
    public void for_0_to_100() {
        Object result = Transpile.$("" +
                "function Hello(): int64 {\n" +
                "   var total: int64\n" +
                "   for i := 0; i < 100; i++ {" +
                "       total = total + i" +
                "   }" +
                "   return total\n" +
                "}");
        Assert.assertEquals("hello", result);
    }
}
