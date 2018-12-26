package com.dexscript.ast.expr;

import com.dexscript.test.framework.TestFramework;
import org.junit.Assert;
import org.junit.Test;

public class DexArrayExprTest {

    @Test
    public void empty() {
        TestFramework.assertParsedAST(DexArrayExpr::$);
    }

    @Test
    public void one_element() {
        TestFramework.assertParsedAST(DexArrayExpr::$);
    }

    @Test
    public void two_elements() {
        TestFramework.assertParsedAST(DexArrayExpr::$);
    }

    @Test
    public void missing_element() {
        TestFramework.assertParsedAST(DexArrayExpr::$);
    }

    @Test
    public void missing_right_bracket() {
        TestFramework.assertParsedAST(DexArrayExpr::$);
    }
}
