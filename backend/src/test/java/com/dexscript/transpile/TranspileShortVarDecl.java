package com.dexscript.transpile;

import org.junit.Assert;
import org.junit.Test;

public class TranspileShortVarDecl {

    @Test
    public void string_const_widen_to_string() {
        Object result = Transpile.$("" +
                "function Hello(): string {" +
                "   msg := 'hello'\n" +
                "   return msg\n" +
                "}");
        Assert.assertEquals("hello", result);
    }

    @Test
    public void integer_const_widen_to_int64() {
        Long result = (Long) Transpile.$("" +
                "function Hello(): int64 {" +
                "   val := 100\n" +
                "   return val\n" +
                "}");
        Assert.assertEquals(Long.valueOf(100), result);
    }
}
