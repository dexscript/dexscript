package com.dexscript.ast.expr;

import com.dexscript.test.framework.TestFramework;
import org.junit.Test;

public class DexBoolConstTest {

    @Test
    public void matched() {
        TestFramework.assertTrue(src -> DexBoolConst.$(src).matched());
    }

    @Test
    public void unmatched() {
        TestFramework.assertFalse(src -> DexBoolConst.$(src).matched());
    }
}
