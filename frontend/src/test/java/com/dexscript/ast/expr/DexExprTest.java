package com.dexscript.ast.expr;

import com.dexscript.test.framework.TestFramework;
import org.junit.Test;

public class DexExprTest {

    @Test
    public void unary_then_binary() {
        TestFramework.assertObject(DexExpr::$parse);
    }

    @Test
    public void binary_then_unary() {
        TestFramework.assertObject(DexExpr::$parse);
    }

    @Test
    public void add_has_lower_rank_than_mul_1() {
        TestFramework.assertObject(DexExpr::$parse);
    }

    @Test
    public void add_has_lower_rank_than_mul_2() {
        TestFramework.assertObject(DexExpr::$parse);
    }

    @Test
    public void mul_and_div_has_equal_rank_1() {
        TestFramework.assertObject(DexExpr::$parse);
    }

    @Test
    public void mul_and_div_has_equal_rank_2() {
        TestFramework.assertObject(DexExpr::$parse);
    }

    @Test
    public void paren_override_rank() {
        TestFramework.assertObject(DexExpr::$parse);
    }

    @Test
    public void float_literal_is_preferred_over_integer_literal_1() {
        TestFramework.assertObject(DexExpr::$parse);
    }

    @Test
    public void float_literal_is_preferred_over_integer_literal_2() {
        TestFramework.assertObject(DexExpr::$parse);
    }

    @Test
    public void mix_function_call_and_method_call() {
        TestFramework.assertObject(DexExpr::$parse);
    }
}
