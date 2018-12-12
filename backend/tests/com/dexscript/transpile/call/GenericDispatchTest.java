package com.dexscript.transpile.call;

import com.dexscript.transpile.Transpile;
import org.junit.Assert;
import org.junit.Test;

public class GenericDispatchTest {

    @Test
    public void call_generic_function() {
        Object result = Transpile.$("" +
                "function Hello() {\n" +
                "   return World('hello')\n" +
                "}\n" +
                "function World(<T>: string, msg: T): T {\n" +
                "   return msg\n" +
                "}");
        Assert.assertEquals("hello", result);
    }

    @Test
    public void call_generic_function_with_type_arg() {
        Object result = Transpile.$("" +
                "function Hello() {\n" +
                "   return World<int64>('hello')\n" +
                "}\n" +
                "function World(<T>: string, msg: T): T {\n" +
                "   return msg\n" +
                "}");
        Assert.assertEquals("hello", result);
    }
}
