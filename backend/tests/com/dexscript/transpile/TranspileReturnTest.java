package com.dexscript.transpile;

import com.dexscript.runtime.Result;
import org.junit.Assert;
import org.junit.Test;

public class TranspileReturnTest {

    @Test
    public void return_string_literal() {
        Result result = Transpile.$("" +
                "function Hello(): string {\n" +
                "   return 'hello'\n" +
                "}");
        Assert.assertEquals("hello", result.value());
    }
}
