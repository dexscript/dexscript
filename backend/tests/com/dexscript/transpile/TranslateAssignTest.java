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
}
