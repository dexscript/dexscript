package com.dexscript.ast.expr;

import org.junit.Assert;
import org.junit.Test;

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

    private DexExpr left(DexExpr expr) {
        return ((DexBinaryOperator) expr).left();
    }

    private DexExpr right(DexExpr expr) {
        return ((DexBinaryOperator) expr).right();
    }

    @Test
    public void paren_override_rank() {
        DexMulExpr expr = (DexMulExpr) DexExpr.parse("(a+b)*c");
        Assert.assertEquals("(a+b)", expr.left().toString());
        Assert.assertEquals("c", expr.right().toString());
    }

    @Test
    public void float_literal_is_preferred_over_integer_literal() {
        Assert.assertEquals("1.34e100", DexExpr.parse("1.34e100").toString());
        DexIntegerLiteral parsed = (DexIntegerLiteral) DexExpr.parse("100");
        Assert.assertEquals("100", parsed.toString());
    }

    @Test
    public void mix_function_call_and_method_call() {
        DexMethodCallExpr methodCallExpr = (DexMethodCallExpr) DexExpr.parse("a().b()");
        Assert.assertEquals("a().b()", methodCallExpr.toString());
        Assert.assertEquals("a()", methodCallExpr.obj().toString());
        Assert.assertEquals("a", methodCallExpr.obj().asFunctionCall().target().toString());
    }
}
