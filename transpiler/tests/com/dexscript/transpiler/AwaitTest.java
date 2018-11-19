package com.dexscript.transpiler;

import org.junit.Assert;
import org.junit.Test;

public class AwaitTest extends TranspilerTest {

    @Test
    public void test_get_result() {
        String src = "" +
                "function Hello(): string {\n" +
                "    w := World{}\n" +
                "    return <- w\n" +
                "}\n" +
                "\n" +
                "function World(): string {\n" +
                "    return 'hello'\n" +
                "}";
        Assert.assertEquals("hello", transpile1(src));
    }

    @Test
    public void test_await_without_blocking() {
        String src = "" +
                "function Hello(): string {\n" +
                "    w := World{}\n" +
                "    return w.Say()\n" +
                "}\n" +
                "\n" +
                "function World() {\n" +
                "    await {\n" +
                "    -> Say(): string {\n" +
                "        return 'hello'\n" +
                "    }}\n" +
                "}";
        Assert.assertEquals("hello", transpile1(src));
    }

    @Test
    public void test_await_with_assignment() {
        String src = "" +
                "function Hello(): string {\n" +
                "   w := World{}\n" +
                "   w.Say('hello')\n" +
                "   return <-w\n" +
                "}\n" +
                "\n" +
                "function World():string {\n" +
                "   var msg: string\n" +
                "   await {\n" +
                "   -> Say(msg_: string) {\n" +
                "       msg = msg_\n" +
                "   }}\n" +
                "   return msg\n" +
                "}";
        Assert.assertEquals("hello", transpile1(src));
    }

    @Test
    public void test_pass_result() {
        String src = "" +
                "function Hello(): string {\n" +
                "    w := World{}\n" +
                "    return Dummy(w)\n" +
                "}\n" +
                "\n" +
                "function World(): string {\n" +
                "    return 'hello'\n" +
                "}\n" +
                "\n" +
                "function Dummy(w: World): string {\n" +
                "   return <-w\n" +
                "}";
        Assert.assertEquals("hello", transpile1(src));
    }

    @Test
    public void test_await_with_blocking() {
        String src = "" +
                "function Hello(): string {\n" +
                "   w := World{}\n" +
                "   d := Dummy{w}\n" +
                "   w.setMessage('hello')\n" +
                "   return <-d\n" +
                "}\n" +
                "\n" +
                "function World(): string {\n" +
                "   var msg: string\n" +
                "   await {\n" +
                "   -> setMessage(msg_: string) {\n" +
                "       msg = msg_\n" +
                "   }}\n" +
                "   return msg\n" +
                "}\n" +
                "\n" +
                "function Dummy(w: World): string {\n" +
                "   return <-w\n" +
                "}";
        System.out.println(src);
        Assert.assertEquals("hello", transpile1(src));
    }
}
