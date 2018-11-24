package com.dexscript.transpile;

import com.dexscript.runtime.Result;
import org.junit.Assert;
import org.junit.Test;

public class FunctionCallTest {
    @Test
    public void defined_after_me() {
        String src = "" +
                "function hello(): string {\n" +
                "   return A()\n" +
                "}\n" +
                "function A(): string{\n" +
                "   return 'a'\n" +
                "}";
        Result result = TranspileOne.__(src);
        Assert.assertEquals("a", result.value());
    }
}
