package com.dexscript.ast.expr;

import com.dexscript.test.framework.TestFramework;
import org.junit.Test;

public class DexArrayExprTest {

    @Test
    public void empty() {
        TestFramework.assertObject(DexArrayExpr::$);
    }

    @Test
    public void one_element() {
        TestFramework.assertObject(DexArrayExpr::$);
    }

    @Test
    public void two_elements() {
        TestFramework.assertObject(DexArrayExpr::$);
    }

    @Test
    public void missing_element() {
        TestFramework.assertObject(DexArrayExpr::$);
    }

    @Test
    public void missing_right_bracket() {
        TestFramework.assertObject(DexArrayExpr::$);
    }
}
