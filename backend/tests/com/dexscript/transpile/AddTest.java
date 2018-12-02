package com.dexscript.transpile;

import com.dexscript.runtime.Result;
import org.junit.Assert;
import org.junit.Test;

public class AddTest {
    @Test
    public void one_plus_one() {
        String src = "" +
                "function Hello(): int64 {\n" +
                "   return 1+1\n" +
                "}";
        Result res = TranspileOne.__(src);
        Assert.assertEquals(2L, res.value());
    }
}
