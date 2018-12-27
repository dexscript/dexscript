package com.dexscript.ast.expr;

import com.dexscript.test.framework.TestFramework;
import org.junit.Test;

public class DexConsumeExprTest {

    @Test
    public void matched() {
        TestFramework.assertObject(DexExpr::$parse);
    }

    @Test
    public void consume_new_actor() {
        TestFramework.assertObject(DexExpr::$parse);
    }
}
