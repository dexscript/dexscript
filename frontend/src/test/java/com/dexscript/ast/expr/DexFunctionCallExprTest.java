package com.dexscript.ast.expr;

import com.dexscript.test.framework.TestFramework;
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
    public void invalid_argument_recover_by_comma() {
        TestFramework.assertParsedAST(DexExpr::$parse);
    }

    @Test
    public void invalid_argument_recover_by_line_end() {
        TestFramework.assertParsedAST(DexExpr::$parse);
    }

    @Test
    public void invalid_argument_recover_by_file_end() {
        TestFramework.assertParsedAST(DexExpr::$parse);
    }

    @Test
    public void argument_followed_by_invalid_argument() {
        TestFramework.assertParsedAST(DexExpr::$parse);
    }

    @Test
    public void missing_right_paren_recover_by_file_end() {
        TestFramework.assertParsedAST(DexExpr::$parse);
    }

    @Test
    public void missing_right_paren_recover_by_line_end() {
        TestFramework.assertParsedAST(DexExpr::$parse);
    }

    @Test
    public void missing_named_arg_val() {
        TestFramework.assertParsedAST(DexExpr::$parse);
    }
}
