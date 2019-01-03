package com.dexscript.ast.expr;

import com.dexscript.test.framework.TestFramework;
import org.junit.Test;

public class DexNewExprTest {

    @Test
    public void zero_argument() {
        TestFramework.assertParsedAST(DexExpr::$parse);
    }

    @Test
    public void three_pos_args() {
        TestFramework.assertParsedAST(DexExpr::$parse);
    }

    @Test
    public void missing_target() {
        TestFramework.assertParsedAST(DexExpr::$parse);
    }

    @Test
    public void missing_function_call() {
        TestFramework.assertParsedAST(DexExpr::$parse);
    }

    @Test
    public void prefix_with_double_colon() {
        TestFramework.assertParsedAST(DexExpr::$parse);
    }
}
