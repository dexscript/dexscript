package com.dexscript.ast.expr;

import com.dexscript.test.framework.TestFramework;
import org.junit.Test;

public class DexParenExprTest {

    @Test
    public void matched() {
        TestFramework.assertObject(DexParenExpr::$);
    }

    @Test
    public void empty_paren() {
        TestFramework.assertObject(DexParenExpr::$);
    }

    @Test
    public void nested_empty_paren() {
        TestFramework.assertObject(DexParenExpr::$);
    }

    @Test
    public void missing_right_paren_after_body() {
        TestFramework.assertObject(DexParenExpr::$);
    }

    @Test
    public void missing_right_paren_without_body() {
        TestFramework.assertObject(DexParenExpr::$);
    }
}
