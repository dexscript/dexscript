package com.dexscript.ast.expr;

import com.dexscript.test.framework.TestFramework;
import org.junit.Test;

public class DexEqualExprTest {

    @Test
    public void matched() {
        TestFramework.assertObject(DexExpr::$parse);
    }
}
