package com.dexscript.parser2;

import org.junit.Assert;
import org.junit.Test;

public class DexParenExprTest {

    @Test
    public void matched() {
        Assert.assertEquals("a", new DexParenExpr("(a)").body().toString());
    }

    @Test
    public void empty_paren() {
        DexParenExpr emptyParen = new DexParenExpr("()");
        Assert.assertEquals("<unmatched>)</unmatched>", emptyParen.body().toString());
        Assert.assertTrue(emptyParen.matched());
        Assert.assertEquals("()", emptyParen.toString());
    }

    @Test
    public void nested_empty_paren() {
        DexParenExpr emptyParen = new DexParenExpr("(())");
        Assert.assertEquals("()", emptyParen.body().toString());
        Assert.assertTrue(emptyParen.matched());
        Assert.assertEquals("(())", emptyParen.toString());
    }

    @Test
    public void missing_right_paren_after_body() {
        DexParenExpr paren = new DexParenExpr("((a+b)*c?;b");
        Assert.assertEquals("(a+b)*c", paren.body().toString());
        Assert.assertTrue(paren.matched());
        Assert.assertEquals("((a+b)*c?", paren.toString());
    }

    @Test
    public void missing_right_paren_without_body() {
        DexParenExpr paren = new DexParenExpr("(???\nb");
        Assert.assertEquals("<unmatched>???\nb</unmatched>", paren.body().toString());
        Assert.assertTrue(paren.matched());
        Assert.assertEquals("(???", paren.toString());
    }
}
