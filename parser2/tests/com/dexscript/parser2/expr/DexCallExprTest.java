package com.dexscript.parser2.expr;

import com.dexscript.parser2.expr.DexCallExpr;
import com.dexscript.parser2.expr.DexExpr;
import org.junit.Assert;
import org.junit.Test;

public class DexCallExprTest {

    @Test
    public void three_arguments() {
        DexCallExpr call = (DexCallExpr) DexExpr.parse("print(a1,b1,c1)");
        Assert.assertEquals("print(a1,b1,c1)", call.toString());
        Assert.assertEquals("print", call.target().toString());
        Assert.assertEquals(3, call.args().size());
        Assert.assertEquals("a1", call.args().get(0).toString());
        Assert.assertEquals("b1", call.args().get(1).toString());
        Assert.assertEquals("c1", call.args().get(2).toString());
    }

    @Test
    public void one_argument() {
        DexCallExpr call = (DexCallExpr) DexExpr.parse("print(a)");
        Assert.assertEquals("print(a)", call.toString());
        Assert.assertEquals("print", call.target().toString());
        Assert.assertEquals(1, call.args().size());
        Assert.assertEquals("a", call.args().get(0).toString());
    }

    @Test
    public void zero_argument() {
        DexCallExpr call = (DexCallExpr) DexExpr.parse("print()");
        Assert.assertEquals("print()", call.toString());
        Assert.assertEquals("print", call.target().toString());
        Assert.assertEquals(0, call.args().size());
    }

    @Test
    public void argument_with_extra_comma() {
        DexCallExpr call = (DexCallExpr) DexExpr.parse("print(a,)");
        Assert.assertEquals("print(a,)", call.toString());
        Assert.assertEquals("print", call.target().toString());
        Assert.assertEquals(1, call.args().size());
        Assert.assertEquals("a", call.args().get(0).toString());
    }

    @Test
    public void with_invalid_argument_recover_by_right_paren() {
        DexCallExpr call = (DexCallExpr) DexExpr.parse("print(?)a");
        Assert.assertEquals("print(<error/>?)", call.toString());
        Assert.assertEquals("print", call.target().toString());
        Assert.assertEquals(1, call.args().size());
        Assert.assertEquals("<unmatched>?)a</unmatched>", call.args().get(0).toString());
    }

    @Test
    public void with_invalid_argument_recover_by_comma() {
        DexCallExpr call = (DexCallExpr) DexExpr.parse("print(?,)a");
        Assert.assertEquals("print(<error/>?,)", call.toString());
        Assert.assertEquals("print", call.target().toString());
        Assert.assertEquals(1, call.args().size());
        Assert.assertEquals("<unmatched>?,)a</unmatched>", call.args().get(0).toString());
    }

    @Test
    public void with_invalid_argument_recover_by_line_end() {
        DexCallExpr call = (DexCallExpr) DexExpr.parse("print(?\na");
        Assert.assertEquals("print(<error/>?", call.toString());
        Assert.assertEquals("print", call.target().toString());
        Assert.assertEquals(1, call.args().size());
        Assert.assertEquals("<unmatched>?\na</unmatched>", call.args().get(0).toString());
    }

    @Test
    public void with_invalid_argument_recover_by_file_end() {
        DexCallExpr call = (DexCallExpr) DexExpr.parse("print(?");
        Assert.assertEquals("print(<error/>?", call.toString());
        Assert.assertEquals("print", call.target().toString());
        Assert.assertEquals(1, call.args().size());
        Assert.assertEquals("<unmatched>?</unmatched>", call.args().get(0).toString());
    }

    @Test
    public void valid_argument_followed_by_invalid_argument() {
        DexCallExpr call = (DexCallExpr) DexExpr.parse("print(?,a)");
        Assert.assertEquals("print(<error/>?,a)", call.toString());
        Assert.assertEquals("print", call.target().toString());
        Assert.assertEquals(2, call.args().size());
        Assert.assertEquals("<unmatched>?,a)</unmatched>", call.args().get(0).toString());
        Assert.assertEquals("a", call.args().get(1).toString());
    }

    @Test
    public void missing_right_paren_recover_by_file_end() {
        DexCallExpr call = (DexCallExpr) DexExpr.parse("print(a");
        Assert.assertEquals("print(a<error/>", call.toString());
        Assert.assertEquals("print", call.target().toString());
        Assert.assertEquals(1, call.args().size());
        Assert.assertEquals("a", call.args().get(0).toString());
    }

    @Test
    public void missing_right_paren_recover_by_line_end() {
        DexCallExpr call = (DexCallExpr) DexExpr.parse("print(a;b");
        Assert.assertEquals("print(a<error/>", call.toString());
        Assert.assertEquals("print", call.target().toString());
        Assert.assertEquals(1, call.args().size());
        Assert.assertEquals("a", call.args().get(0).toString());
    }
}
