package com.dexscript.transpile;

import com.dexscript.runtime.Result;
import org.junit.Assert;
import org.junit.Test;

public class ShortVarDeclTest {

    @Test
    public void example() {
        String src = "" +
                "function Hello(): string {\n" +
                "   a := 'hello'\n" +
                "   return a\n" +
                "}";
        Result result = TranspileOne.__(src);
        Assert.assertEquals("hello", result.value());
    }
}
