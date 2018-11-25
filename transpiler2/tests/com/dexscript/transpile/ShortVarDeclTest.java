package com.dexscript.transpile;

import com.dexscript.runtime.Result;
import org.junit.Assert;
import org.junit.Test;

public class ShortVarDeclTest {

    @Test
    public void infer_type_from_string_literal() {
        String src = "" +
                "function Hello(): string {\n" +
                "   a := 'hello'\n" +
                "   return a\n" +
                "}";
        Result result = TranspileOne.__(src);
        Assert.assertEquals("hello", result.value());
    }
}
