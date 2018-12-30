package com.dexscript.ast.expr;

import com.dexscript.test.framework.TestFramework;
import org.junit.Test;

public class DexIndexExprTest {

    @Test
    public void matched() {
        TestFramework.assertParsedAST(DexExpr::$parse);
    }

    @Test
    public void invocation() {
        TestFramework.assertParsedAST(DexExpr::$parse);
    }
}
