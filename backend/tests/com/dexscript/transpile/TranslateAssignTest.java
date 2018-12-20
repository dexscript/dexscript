package com.dexscript.transpile;

import org.junit.Assert;
import org.junit.Test;

public class TranslateAssignTest {

    @Test
    public void assign_string_const_type_to_literal_type() {
        Object result = Transpile.$("" +
                "function Hello(): 'hello' {\n" +
                "   var msg: 'hello'\n" +
                "   msg = 'hello'\n" +
                "   return msg\n" +
                "}");
        Assert.assertEquals("hello", result);
    }

    @Test
    public void assign_string_const_type_to_string_type() {
        Object result = Transpile.$("" +
                "function Hello(): string {\n" +
                "   var msg: string\n" +
                "   msg = 'hello'\n" +
                "   return msg\n" +
                "}");
        Assert.assertEquals("hello", result);
    }

    @Test
    public void assign_integer_const_type_to_literal_type() {
        Object result = Transpile.$("" +
                "function Hello(): 1 {\n" +
                "   var msg: 1\n" +
                "   msg = 1\n" +
                "   return msg\n" +
                "}");
        Assert.assertEquals(1L, result);
    }

    @Test
    public void assign_integer_const_type_to_int64() {
        Object result = Transpile.$("" +
                "function Hello(): int64 {\n" +
                "   var msg: int64\n" +
                "   msg = 1\n" +
                "   return msg\n" +
                "}");
        Assert.assertEquals(1L, result);
    }

    @Test
    public void assign_integer_const_type_to_int32() {
        Object result = Transpile.$("" +
                "function Hello(): int32 {\n" +
                "   var msg: int32\n" +
                "   msg = 1\n" +
                "   return msg\n" +
                "}");
        Assert.assertEquals(1, result);
    }
}
