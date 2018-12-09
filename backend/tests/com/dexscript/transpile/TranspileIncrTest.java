package com.dexscript.transpile;

import org.junit.Assert;
import org.junit.Test;

public class TranspileIncrTest {

    @Test
    public void incr() {
        Object result = Transpile.$("function Hello(): int64 {\n" +
                "   i := 0\n" +
                "   i++\n" +
                "   return i\n" +
                "}");
        Assert.assertEquals(1L, result);
    }
}
