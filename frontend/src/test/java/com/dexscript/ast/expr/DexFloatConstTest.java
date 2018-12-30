package com.dexscript.ast.expr;

import com.dexscript.test.framework.TestFramework;
import org.junit.Test;

public class DexFloatConstTest {

    @Test
    public void matched() {
        TestFramework.assertTrue(src -> DexFloatConst.$(src).matched());
    }

    @Test
    public void with_error() {
        TestFramework.assertParsedAST(DexFloatConst::$);
    }
}
