package com.dexscript.transpile.actor;

import com.dexscript.transpile.TestTranspile;
import org.junit.Assert;
import org.junit.Test;

public class AwaitTest {

    @Test
    public void consume_new() {
        Object result = TestTranspile.$("" +
                "function Hello(): string {\n" +
                "   return <-new World()\n" +
                "}\n" +
                "function World(): string {\n" +
                "   return 'hello world'\n" +
                "}");
        Assert.assertEquals("hello world", result);
    }

    @Test
    public void resolve_integer_const_to_int32() {
        Object result = TestTranspile.$("" +
                "function Hello(): int32 {\n" +
                "   a := new A()\n" +
                "   return a.AA()\n" +
                "}\n" +
                "function A() {\n" +
                "   await {\n" +
                "   case AA(): int32 {\n" +
                "       resolve 100 -> AA\n" +
                "   }}\n" +
                "}\n");
        Assert.assertEquals(100, result);
    }

    @Test
    public void non_block() {
        Object result = TestTranspile.$("" +
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
        Object result = TestTranspile.$("" +
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
