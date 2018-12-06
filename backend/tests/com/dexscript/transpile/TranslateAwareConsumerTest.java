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

    @Test
    public void block() {
        // translate to FSM
        Object result = Transpile.$("" +
                "function Hello(): string {\n" +
                "   a := A{}\n" +
                "   b := B{a}\n" +
                "   a.Resume()\n" +
                "   return <-b\n" +
                "}\n" +
                "function A(): string {\n" +
                "   await {\n" +
                "   case Resume() {\n" +
                "   }}\n" +
                "   return 'hello'\n" +
                "}\n" +
                "function B(a: A): string {\n" +
                "   return <-a\n" +
                "}\n");
        Assert.assertEquals("hello", result);
    }
}
