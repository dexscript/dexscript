package com.dexscript.transpile;

import org.junit.Assert;
import org.junit.Test;

public class TranspileReturnTest {

    @Test
    public void return_string_literal() {
        Object result = Transpile.$("" +
                "function Hello(): string {\n" +
                "   return 'hello'\n" +
                "}");
        Assert.assertEquals("hello", result);
    }

    @Test
    public void return_integer_const_as_integer_literal() {
        Long result = (Long) Transpile.$("" +
                "function Hello(): 1 {\n" +
                "   return 1\n" +
                "}");
        Assert.assertEquals(Long.valueOf(1), result);
    }

    @Test
    public void return_integer_const_as_int64() {
        Long result = (Long) Transpile.$("" +
                "function Hello(): int64 {\n" +
                "   return 1\n" +
                "}");
        Assert.assertEquals(Long.valueOf(1), result);
    }

    @Test
    public void return_integer_const_as_int32() {
        Integer result = (Integer) Transpile.$("" +
                "function Hello(): int32 {\n" +
                "   return 1\n" +
                "}");
        Assert.assertEquals(Integer.valueOf(1), result);
    }
}
