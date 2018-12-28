package com.dexscript.transpile;

import org.junit.Assert;
import org.junit.Test;

public class TranspileIfTest {

    @Test
    public void only_if() {
        Object result = Transpile.$("" +
                "function Hello(): string {\n" +
                "   if 1 == 1 {\n" +
                "       return 'hello'\n" +
                "   }\n" +
                "   return 'world'\n" +
                "}");
        Assert.assertEquals("hello", result);
    }

    @Test
    public void if_then_else() {
        Object result = Transpile.$("" +
                "function Hello(): string {\n" +
                "   if 1 == 0 {\n" +
                "       return 'hello'\n" +
                "   } else {\n" +
                "       return 'world'\n" +
                "   }\n" +
                "}");
        Assert.assertEquals("world", result);
    }

    @Test
    public void if_then_else_if() {
        Object result = Transpile.$("" +
                "function Hello(): string {\n" +
                "   if 1 == 0 {\n" +
                "       return 'hello'\n" +
                "   } else if 1 == 1 {\n" +
                "       return 'world'\n" +
                "   }\n" +
                "}");
        Assert.assertEquals("world", result);
    }

    @Test
    public void statements_after_if() {
        Object result = Transpile.$("" +
                "function Hello(): string {\n" +
                "   var msg: string\n" +
                "   if (1 == 0) {\n" +
                "       msg = 'hello'\n" +
                "   } else if (1 == 1) {\n" +
                "       msg = 'world'\n" +
                "   }" +
                "   return msg\n" +
                "}");
        Assert.assertEquals("world", result);
    }
}
