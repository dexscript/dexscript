package com.dexscript.ast.expr;

import org.junit.Assert;
import org.junit.Test;

public class DexFunctionCallExprTest {

    @Test
    public void three_arguments() {
        DexFunctionCallExpr call = (DexFunctionCallExpr) DexExpr.parse("print(a1,b1,c1)");
        Assert.assertEquals("print(a1,b1,c1)", call.toString());
        Assert.assertEquals("print", call.target().toString());
        Assert.assertEquals(3, call.args().size());
        Assert.assertEquals("a1", call.args().get(0).toString());
        Assert.assertEquals("b1", call.args().get(1).toString());
        Assert.assertEquals("c1", call.args().get(2).toString());
    }

    @Test
    public void one_argument() {
        DexFunctionCallExpr call = (DexFunctionCallExpr) DexExpr.parse("print(a)");
        Assert.assertEquals("print(a)", call.toString());
        Assert.assertEquals("print", call.target().toString());
        Assert.assertEquals(1, call.args().size());
        Assert.assertEquals("a", call.args().get(0).toString());
    }

    @Test
    public void zero_argument() {
        DexFunctionCallExpr call = (DexFunctionCallExpr) DexExpr.parse("print()");
        Assert.assertEquals("print()", call.toString());
        Assert.assertEquals("print", call.target().toString());
        Assert.assertEquals(0, call.args().size());
    }

    @Test
    public void argument_with_extra_comma() {
        DexFunctionCallExpr call = (DexFunctionCallExpr) DexExpr.parse("print(a,)");
        Assert.assertEquals("print(a,)", call.toString());
        Assert.assertEquals("print", call.target().toString());
        Assert.assertEquals(1, call.args().size());
        Assert.assertEquals("a", call.args().get(0).toString());
    }

    @Test
    public void with_invalid_argument_recover_by_right_paren() {
        DexFunctionCallExpr call = (DexFunctionCallExpr) DexExpr.parse("print(?)a");
        Assert.assertEquals("print(<error/>?)", call.toString());
        Assert.assertEquals("print", call.target().toString());
        Assert.assertEquals(1, call.args().size());
    }

    @Test
    public void with_invalid_argument_recover_by_comma() {
        DexFunctionCallExpr call = (DexFunctionCallExpr) DexExpr.parse("print(?,)a");
        Assert.assertEquals("print(<error/>?,)", call.toString());
        Assert.assertEquals("print", call.target().toString());
        Assert.assertEquals(1, call.args().size());
        Assert.assertEquals("<error/>", call.args().get(0).toString());
    }

    @Test
    public void with_invalid_argument_recover_by_line_end() {
        DexFunctionCallExpr call = (DexFunctionCallExpr) DexExpr.parse("print(?\na");
        Assert.assertEquals("print(<error/>?", call.toString());
        Assert.assertEquals("print", call.target().toString());
        Assert.assertEquals(1, call.args().size());
        Assert.assertEquals("<error/>", call.args().get(0).toString());
    }

    @Test
    public void with_invalid_argument_recover_by_file_end() {
        DexFunctionCallExpr call = (DexFunctionCallExpr) DexExpr.parse("print(?");
        Assert.assertEquals("print(<error/>?", call.toString());
        Assert.assertEquals("print", call.target().toString());
        Assert.assertEquals(1, call.args().size());
    }

    @Test
    public void valid_argument_followed_by_invalid_argument() {
        DexFunctionCallExpr call = (DexFunctionCallExpr) DexExpr.parse("print(?,a)");
        Assert.assertEquals("print(<error/>?,a)", call.toString());
        Assert.assertEquals("print", call.target().toString());
        Assert.assertEquals(2, call.args().size());
        Assert.assertEquals("a", call.args().get(1).toString());
    }

    @Test
    public void missing_right_paren_recover_by_file_end() {
        DexFunctionCallExpr call = (DexFunctionCallExpr) DexExpr.parse("print(a");
        Assert.assertEquals("print(a<error/>", call.toString());
        Assert.assertEquals("print", call.target().toString());
        Assert.assertEquals(1, call.args().size());
        Assert.assertEquals("a", call.args().get(0).toString());
    }

    @Test
    public void missing_right_paren_recover_by_line_end() {
        DexFunctionCallExpr call = (DexFunctionCallExpr) DexExpr.parse("print(a;b");
        Assert.assertEquals("print(a<error/>", call.toString());
        Assert.assertEquals("print", call.target().toString());
        Assert.assertEquals(1, call.args().size());
        Assert.assertEquals("a", call.args().get(0).toString());
    }

    @Test
    public void two_call_separated_by_new_line() {
        DexExpr expr = DexExpr.parse("hello()\nworld()");
        Assert.assertEquals("hello()", expr.toString());
    }

    @Test
    public void call_with_type_args() {
        DexExpr expr = DexExpr.parse("Hello<uint8>()");
        Assert.assertEquals("Hello<uint8>()", expr.toString());
    }

    @Test
    public void missing_type_arg() {
        DexExpr expr = DexExpr.parse("Hello<??>()");
        Assert.assertEquals("Hello<<error/>??>()", expr.toString());
        expr = DexExpr.parse("Hello<?? >()");
        Assert.assertEquals("Hello<<error/>?? >()", expr.toString());
    }

    @Test
    public void missing_right_angle_bracket() {
        DexExpr expr = DexExpr.parse("Hello<uint8()");
        Assert.assertEquals("Hello<uint8<error/>()", expr.toString());
        expr = DexExpr.parse("Hello<uint8 ()");
        Assert.assertEquals("Hello<uint8 <error/>()", expr.toString());
    }

    @Test
    public void missing_left_paren() {
        DexExpr expr = DexExpr.parse("Hello<uint8>)");
        Assert.assertEquals("Hello<uint8><error/>)", expr.toString());
    }
}
