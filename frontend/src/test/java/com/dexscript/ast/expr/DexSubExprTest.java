package com.dexscript.ast.expr;

import com.dexscript.test.framework.TestFramework;
import org.junit.Test;

public class DexSubExprTest {

    @Test
    public void matched() {
        TestFramework.assertParsedAST(DexExpr::$parse);
    }

    @Test
    public void with_space() {
        TestFramework.assertParsedAST(DexExpr::$parse);
    }
}
