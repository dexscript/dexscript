package com.dexscript.ast.expr;

import com.dexscript.test.framework.TestFramework;
import org.junit.Test;

public class DexFieldExprTest {

    @Test
    public void matched() {
        TestFramework.assertParsedAST(DexExpr::$parse);
    }
}
