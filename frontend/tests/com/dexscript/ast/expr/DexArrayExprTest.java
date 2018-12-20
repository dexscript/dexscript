package com.dexscript.ast.expr;

import org.junit.Assert;
import org.junit.Test;

public class DexArrayExprTest {

    @Test
    public void empty() {
        Assert.assertEquals("[]", new DexArrayExpr("[]").toString());
    }

    @Test
    public void one_element() {
        DexArrayExpr arrayLiteral = new DexArrayExpr("[1]");
        Assert.assertEquals("[1]", arrayLiteral.toString());
        Assert.assertEquals(1, arrayLiteral.elems().size());
        Assert.assertEquals("1", arrayLiteral.elems().get(0).toString());
    }

    @Test
    public void two_elements() {
        DexArrayExpr arrayLiteral = new DexArrayExpr("[1, 2]");
        Assert.assertEquals("[1, 2]", arrayLiteral.toString());
        Assert.assertEquals(2, arrayLiteral.elems().size());
        Assert.assertEquals("1", arrayLiteral.elems().get(0).toString());
        Assert.assertEquals("2", arrayLiteral.elems().get(1).toString());
    }

    @Test
    public void missing_element() {
        DexArrayExpr arrayLiteral = new DexArrayExpr("[??, 2]");
        Assert.assertEquals("[<error/>??, 2]", arrayLiteral.toString());
    }

    @Test
    public void missing_right_bracket() {
        DexArrayExpr arrayLiteral = new DexArrayExpr("[1, 2");
        Assert.assertEquals("[1, 2<error/>", arrayLiteral.toString());
    }
}
