package com.dexscript.transpile;

import org.junit.Assert;
import org.junit.Test;

public class TranslateAssignTest {

    @Test
    public void declare_then_assign() {
        Object result = Transpile.$("" +
                "function Hello(): string {\n" +
                "   var msg: string\n" +
                "   msg = 'hello'\n" +
                "   return msg\n" +
                "}");
        Assert.assertEquals("hello", result);
    }

    @Test
    public void widen_integer_const_type_to_literal_type() {
        Object result = Transpile.$("" +
                "function Hello(): 1 {\n" +
                "   var msg: 1\n" +
                "   msg = 1\n" +
                "   return msg\n" +
                "}");
        Assert.assertEquals(1L, result);
    }
}
