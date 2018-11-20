package com.dexscript.parser2;

import org.junit.Assert;
import org.junit.Test;

import static com.dexscript.parser2.DexBinaryOperator.left;
import static com.dexscript.parser2.DexBinaryOperator.right;

public class DexExprTest {

    @Test
    public void unary_then_binary() {
        DexAddExpr expr = (DexAddExpr) DexExpr.parse("+a+b");
        Assert.assertEquals("+a", expr.left().toString());
        Assert.assertEquals("b", expr.right().toString());
    }

    @Test
    public void binary_then_unary() {
        DexAddExpr expr = (DexAddExpr) DexExpr.parse("a+-b");
        Assert.assertEquals("a", expr.left().toString());
        Assert.assertEquals("-b", expr.right().toString());
    }

    @Test
    public void add_has_lower_rank_than_mul() {
        DexAddExpr expr = (DexAddExpr) DexExpr.parse("a+b*c");
        Assert.assertEquals("a", expr.left().toString());
        Assert.assertEquals("b*c", expr.right().toString());
        expr = (DexAddExpr) DexExpr.parse("a*b+c");
        Assert.assertEquals("a*b", expr.left().toString());
        Assert.assertEquals("c", expr.right().toString());
    }

    @Test
    public void mul_and_div_has_equal_rank() {
        DexAddExpr expr = (DexAddExpr) DexExpr.parse("a*b/c+d");
        Assert.assertEquals("a*b/c", left(expr).toString());
        Assert.assertEquals("d", right(expr).toString());
        Assert.assertEquals("a*b", left(left(expr)).toString());
        Assert.assertEquals("c", right(left(expr)).toString());

        expr = (DexAddExpr) DexExpr.parse("a/b*c+d");
        Assert.assertEquals("a/b*c", expr.left().toString());
        Assert.assertEquals("d", expr.right().toString());
        Assert.assertEquals("a/b", left(left(expr)).toString());
        Assert.assertEquals("c", right(left(expr)).toString());
    }
}
