package com.dexscript.transpile;

import org.junit.Assert;
import org.junit.Test;

public class TranslateAwareConsumerTest {

    @Test
    public void non_block() {
        Object result = Transpile.$("" +
                "function Hello(): string {\n" +
                "   a := new A()\n" +
                "   return a.AA()\n" +
                "}\n" +
                "function A() {\n" +
                "   await {\n" +
                "   case AA(): string {\n" +
                "       resolve 'hello' -> AA\n" +
                "   }}\n" +
                "}\n");
        Assert.assertEquals("hello", result);
    }

    @Test
    public void block() {
        // translate to FSM
        Object result = Transpile.$("" +
                "function Hello(): string {\n" +
                "   a := new A()\n" +
                "   b := new B(a)\n" +
                "   a.Unpause()\n" +
                "   return <-b\n" +
                "}\n" +
                "function A(): string {\n" +
                "   await {\n" +
                "   case Unpause() {\n" +
                "       resolve -> Unpause" +
                "   }}\n" +
                "   return 'hello'\n" +
                "}\n" +
                "function B(a: A): string {\n" +
                "   return <-a\n" +
                "}\n");
        Assert.assertEquals("hello", result);
    }
}
