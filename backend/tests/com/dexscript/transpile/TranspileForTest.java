package com.dexscript.transpile;

import org.junit.Assert;
import org.junit.Test;

public class TranspileForTest {

    @Test
    public void for_0_to_102400() {
        Object result = Transpile.$("" +
                "function Hello(): int64 {\n" +
                "   var total: int64\n" +
                "   for i := 0; i < 102400; i++ {" +
                "       total = total + i" +
                "   }" +
                "   return total\n" +
                "}");
        Assert.assertEquals(5242828800L, result);
    }

    @Test
    public void for_0_to_10240_with_await() {
        Object result = Transpile.$("" +
                "function Hello(): int64 {\n" +
                "   var total: int64\n" +
                "   for i := 0; i < 102400; i++ {" +
                "       asIs := new AsIs(i)\n" +
                "       total = total + <-asIs" +
                "   }" +
                "   return total\n" +
                "}\n" +
                "function AsIs(i: int64): int64 {\n" +
                "   return i\n" +
                "}");
        Assert.assertEquals(5242828800L, result);
    }
}
