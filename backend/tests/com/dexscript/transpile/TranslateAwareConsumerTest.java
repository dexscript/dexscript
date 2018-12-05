package com.dexscript.transpile;

import org.junit.Assert;
import org.junit.Test;

public class TranslateAwareConsumerTest {

    @Test
    public void non_block() {
        Object result = Transpile.$("" +
                "function Hello(): string {\n" +
                "   a := A{}\n" +
                "   return a.AA()\n" +
                "}\n" +
                "function A() {\n" +
                "   await {\n" +
                "   case AA(): string {\n" +
                "       return 'hello'\n" +
                "   }}\n" +
                "}\n");
        Assert.assertEquals("hello", result);
    }
}
