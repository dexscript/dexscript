package com.dexscript.parser2;

import org.junit.Assert;
import org.junit.Test;

public class DexFunctionTest {

    @Test
    public void empty() {
        String src = "" +
                " function hello() {\n" +
                "}\n";
        DexFunction function = new DexFunction(src);
        Assert.assertTrue(function.matched());
        Assert.assertEquals("hello", function.identifier().toString());
        Assert.assertEquals("()", function.signature().toString());
        Assert.assertEquals("{\n}", function.block().toString());
        Assert.assertEquals(src.substring(1), function.toString());
    }

    @Test
    public void one_argument() {
        String src = "" +
                " function hello(msg:string) {\n" +
                "}\n";
        DexFunction function = new DexFunction(src);
        Assert.assertTrue(function.matched());
        Assert.assertEquals("hello", function.identifier().toString());
        Assert.assertEquals("(msg:string)", function.signature().toString());
        Assert.assertEquals("{\n}", function.block().toString());
        Assert.assertEquals(src.substring(1), function.toString());
    }

    @Test
    public void missing_left_paren() {
        String src = "" +
                "function hello ) {\n" +
                "}\n";
        DexFunction function = new DexFunction(src);
        Assert.assertFalse(function.matched());
        Assert.assertEquals(15, function.err().errorPos);
    }

    @Test
    public void skip_garbage_in_prelude() {
        String src = "" +
                " abc function hello () {\n" +
                "}\n";
        DexFunction function = new DexFunction(src);
        Assert.assertEquals(1, function.err().errorPos);
        Assert.assertTrue(function.err().toString().contains("function"));
    }

    @Test
    public void missing_left_brace() {
        String src = "" +
                "function hello () \n" +
                "}\n";
        DexFunction function = new DexFunction(src);
        function.body().block();
        Assert.assertEquals("()<error/> \n" +
                "}\n", function.body().toString());
    }
}
