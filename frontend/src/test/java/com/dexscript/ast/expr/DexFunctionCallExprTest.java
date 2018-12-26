package com.dexscript.ast.expr;

import com.dexscript.test.framework.TestFramework;
import org.junit.Assert;
import org.junit.Test;

public class DexFunctionCallExprTest {

    @Test
    public void zero_argument() {
        TestFramework.assertParsedAST(DexExpr::$parse);
    }

    @Test
    public void one_pos_arg() {
        TestFramework.assertParsedAST(DexExpr::$parse);
    }

    @Test
    public void three_pos_args() {
        TestFramework.assertParsedAST(DexExpr::$parse);
    }

    @Test
    public void pos_arg_with_extra_comma() {
        TestFramework.assertParsedAST(DexExpr::$parse);
    }

    @Test
    public void two_call_separated_by_new_line() {
        TestFramework.assertParsedAST(DexExpr::$parse);
    }

    @Test
    public void one_type_arg() {
        TestFramework.assertParsedAST(DexExpr::$parse);
    }

    @Test
    public void named_arg_after_pos_arg() {
        TestFramework.assertParsedAST(DexExpr::$parse);
    }

    @Test
    public void two_named_args() {
        TestFramework.assertParsedAST(DexExpr::$parse);
    }

    @Test
    public void leading_named_arg() {
        TestFramework.assertParsedAST(DexExpr::$parse);
    }

    @Test
    public void pos_arg_after_named_arg() {
        TestFramework.assertParsedAST(DexExpr::$parse);
    }

    @Test
    public void context_arg() {
        TestFramework.assertParsedAST(DexExpr::$parse);
    }

    @Test
    public void invalid_pos_arg_recover_by_right_paren() {
        TestFramework.assertParsedAST(DexExpr::$parse);
    }

    @Test
    public void with_invalid_argument_recover_by_comma() {
        DexFunctionCallExpr call = (DexFunctionCallExpr) DexExpr.$parse("print(?,)a");
        Assert.assertEquals("print(<error/>?,)", call.toString());
        Assert.assertEquals("print", call.target().toString());
        Assert.assertEquals(1, call.posArgs().size());
        Assert.assertEquals("<error/>", call.posArgs().get(0).toString());
    }

    @Test
    public void with_invalid_argument_recover_by_line_end() {
        DexFunctionCallExpr call = (DexFunctionCallExpr) DexExpr.$parse("print(?\na");
        Assert.assertEquals("print(<error/>?", call.toString());
        Assert.assertEquals("print", call.target().toString());
        Assert.assertEquals(1, call.posArgs().size());
        Assert.assertEquals("<error/>", call.posArgs().get(0).toString());
    }

    @Test
    public void with_invalid_argument_recover_by_file_end() {
        DexFunctionCallExpr call = (DexFunctionCallExpr) DexExpr.$parse("print(?");
        Assert.assertEquals("print(<error/>?", call.toString());
        Assert.assertEquals("print", call.target().toString());
        Assert.assertEquals(1, call.posArgs().size());
    }

    @Test
    public void valid_argument_followed_by_invalid_argument() {
        DexFunctionCallExpr call = (DexFunctionCallExpr) DexExpr.$parse("print(?,a)");
        Assert.assertEquals("print(<error/>?,a)", call.toString());
        Assert.assertEquals("print", call.target().toString());
        Assert.assertEquals(2, call.posArgs().size());
        Assert.assertEquals("a", call.posArgs().get(1).toString());
    }

    @Test
    public void missing_right_paren_recover_by_file_end() {
        DexFunctionCallExpr call = (DexFunctionCallExpr) DexExpr.$parse("print(a");
        Assert.assertEquals("print(a<error/>", call.toString());
        Assert.assertEquals("print", call.target().toString());
        Assert.assertEquals(1, call.posArgs().size());
        Assert.assertEquals("a", call.posArgs().get(0).toString());
    }

    @Test
    public void missing_right_paren_recover_by_line_end() {
        DexFunctionCallExpr call = (DexFunctionCallExpr) DexExpr.$parse("print(a;b");
        Assert.assertEquals("print(a<error/>", call.toString());
        Assert.assertEquals("print", call.target().toString());
        Assert.assertEquals(1, call.posArgs().size());
        Assert.assertEquals("a", call.posArgs().get(0).toString());
    }

    @Test
    public void missing_type_arg() {
        DexExpr expr = DexExpr.$parse("Hello<??>()");
        Assert.assertEquals("Hello<<error/>??>()", expr.toString());
        expr = DexExpr.$parse("Hello<?? >()");
        Assert.assertEquals("Hello<<error/>?? >()", expr.toString());
    }

    @Test
    public void missing_right_angle_bracket() {
        DexExpr expr = DexExpr.$parse("Hello<uint8()");
        Assert.assertEquals("Hello<uint8<error/>()", expr.toString());
        expr = DexExpr.$parse("Hello<uint8 ()");
        Assert.assertEquals("Hello<uint8 <error/>()", expr.toString());
    }

    @Test
    public void missing_left_paren() {
        DexExpr expr = DexExpr.$parse("Hello<uint8>)");
        Assert.assertEquals("Hello<uint8><error/>)", expr.toString());
    }

    @Test
    public void missing_named_arg_val() {
        DexFunctionCallExpr call = (DexFunctionCallExpr) DexExpr.$parse("print(d=)");
        Assert.assertEquals("print(d=<error/>)", call.toString());
        Assert.assertEquals(0, call.posArgs().size());
        Assert.assertEquals(0, call.namedArgs().size());
    }
}
