package com.dexscript.transpile;

import com.dexscript.runtime.Result;
import org.junit.Assert;
import org.junit.Test;

public class ReturnTest {

    @Test
    public void return_string() {
        String src = "" +
                "function Hello(): string {\n" +
                "   return 'hello'\n" +
                "}";
        Result res = TranspileOne.__(src);
        Assert.assertEquals("hello", res.value());
    }
}
