package com.dexscript.ast.expr;

import com.dexscript.test.framework.TestFramework;
import org.junit.Test;

public class DexPositiveExprTest {

    @Test
    public void matched() {
        TestFramework.assertTrue(src -> DexPositiveExpr.$(src).matched());
    }

    @Test
    public void unmatched() {
        TestFramework.assertFalse(src -> DexPositiveExpr.$(src).matched());
    }
}
