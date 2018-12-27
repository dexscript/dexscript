package com.dexscript.ast.expr;

import com.dexscript.test.framework.TestFramework;
import org.junit.Test;

public class DexMethodCallExprTest {

    @Test
    public void matched() {
        TestFramework.assertObject(DexExpr::$parse);
    }
}
