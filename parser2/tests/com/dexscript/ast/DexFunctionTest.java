package com.dexscript.ast;

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
        Assert.assertEquals("()", function.sig().toString());
        Assert.assertEquals("{\n}", function.block().toString());
        Assert.assertEquals(src.substring(1), function.toString());
    }

    @Test
    public void no_space_between_function_keyword_and_identifier() {
        Assert.assertEquals("<unmatched>functionhello() {}</unmatched>", new DexFunction("functionhello() {}").toString());
    }

    @Test
    public void one_argument() {
        String src = "" +
                " function hello(msg:string) {\n" +
                "}\n";
        DexFunction function = new DexFunction(src);
        Assert.assertTrue(function.matched());
        Assert.assertEquals("hello", function.identifier().toString());
        Assert.assertEquals("(msg:string)", function.sig().toString());
        Assert.assertEquals("{\n}", function.block().toString());
        Assert.assertEquals(src.substring(1), function.toString());
    }

    @Test
    public void missing_left_paren() {
        String src = "" +
                "function hello ) {\n" +
                "}\n";
        Assert.assertEquals("" +
                "<unmatched>function hello ) {\n" +
                "}\n" +
                "</unmatched>", new DexFunction(src).toString());
    }

    @Test
    public void skip_garbage_in_prelude() {
        String src = "" +
                " abc function hello () {\n" +
                "}\n";
        Assert.assertEquals("" +
                "<unmatched> abc function hello () {\n" +
                "}\n" +
                "</unmatched>", new DexFunction(src).toString());
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
