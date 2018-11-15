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

    private Object transpile1(String source) {
        try {
            Class clazz = transpiler.transpile("hello", "package abc\n" + source).get("abc.Hello");
            return ((Result1) clazz.getConstructor().newInstance()).result1__();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void transpile0(String source) {
        try {
            Class clazz = transpiler.transpile("hello", "package abc\n" + source).get("abc.Hello");
            clazz.getConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testReturnStringLiteral() {
        String src = "" +
                "function Hello(): string {\n" +
                "   return 'hello'\n" +
                "}\n";
        Assert.assertEquals("hello", transpile1(src));
    }

    @Test
    public void testFunctionCall() {
        String src = "" +
                "function Hello(): string {\n" +
                "   return World()\n" +
                "}\n" +
                "function World(): string {\n" +
                "   return 'hello'\n" +
                "}\n";
        Assert.assertEquals("hello", transpile1(src));
    }

    @Test
    public void testPlus() {
        String src = "" +
                "function Hello(): int64 {\n" +
                "   return 1+2\n" +
                "}\n";
        Assert.assertEquals((long) 3, transpile1(src));
    }

    @Test
    public void testAssignment() {
        String src = "" +
                "function Hello() {\n" +
                "   val := 'hello'\n" +
                "}\n";
        transpile0(src);
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
