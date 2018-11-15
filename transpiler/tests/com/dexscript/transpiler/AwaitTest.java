package com.dexscript.transpiler;

import org.junit.Assert;
import org.junit.Test;

public class AwaitTest extends TranspilerTest {

    @Test
    public void testGetResult() {
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
    public void testAwait() {
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
}
