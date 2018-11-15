package com.dexscript.transpiler;

import com.dexscript.runtime.Result1;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TranspilerTest {

    private Transpiler transpiler;

    @Before
    public void setup() {
        transpiler = new Transpiler();
    }

    @After
    public void teardown() {
        transpiler.close();
    }

    @Test
    public void testReturnStringLiteral() throws Exception {
        String src = "" +
                "function Hello(): string {\n" +
                "   return 'hello'\n" +
                "}\n";
        Assert.assertEquals("hello", transpile1(src));
    }

    private Object transpile1(String source) throws Exception {
        Class clazz = transpiler.transpile("hello", "package abc\n" + source).get("abc.Hello");
        return ((Result1) clazz.getConstructor().newInstance()).result1__();
    }

    @Test
    public void testFunctionCall() {
        transpiler.transpile("hello", "" +
                "package abc\n" +
                "function Hello(): string {\n" +
                "   return World()\n" +
                "}\n" +
                "function World(): string {\n" +
                "   return 'hello'\n" +
                "}\n");
    }

    @Test
    public void testPlus() {
        transpiler.transpile("hello", "" +
                "package abc\n" +
                "function Hello(): int64 {\n" +
                "   return 1+2\n" +
                "}\n");
    }

    @Test
    public void testAssignment() {
        transpiler.transpile("hello", "" +
                "package abc\n" +
                "function Hello() {\n" +
                "   val := 'hello'\n" +
                "}\n");
    }

    @Test
    public void testGetResult() {
        transpiler.transpile("hello", "" +
                "package abc\n" +
                "\n" +
                "function hello(): string {\n" +
                "    w := world{}\n" +
                "    return <- w\n" +
                "}\n" +
                "\n" +
                "function world(): string {\n" +
                "    return 'hello'\n" +
                "}");
    }

    @Test
    public void testAwait() {
        transpiler.transpile("hello", "" +
                "package abc\n" +
                "\n" +
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
                "}");
    }
}
