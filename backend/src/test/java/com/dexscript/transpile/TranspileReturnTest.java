package com.dexscript.transpile;

import com.dexscript.test.framework.TestFramework;
import org.junit.Assert;
import org.junit.Test;

public class TranspileReturnTest {

    @Test
    public void return_string() {
        TestFramework.assertByList(Transpile::$);
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
