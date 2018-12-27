package com.dexscript.ast.expr;

import com.dexscript.test.framework.TestFramework;
import org.junit.Test;

public class DexNewExprTest {

    @Test
    public void zero_argument() {
        TestFramework.assertObject(DexExpr::$parse);
    }

    @Test
    public void three_pos_args() {
        TestFramework.assertObject(DexExpr::$parse);
    }

    @Test
    public void missing_target() {
        TestFramework.assertObject(DexExpr::$parse);
    }

    @Test
    public void missing_function_call() {
        TestFramework.assertObject(DexExpr::$parse);
    }
}
