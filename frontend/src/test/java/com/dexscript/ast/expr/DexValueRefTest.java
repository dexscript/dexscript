package com.dexscript.ast.expr;

import com.dexscript.test.framework.TestFramework;
import org.junit.Test;

public class DexValueRefTest {

    @Test
    public void matched() {
        TestFramework.assertTrue(src -> DexValueRef.$(src).matched());
    }

    @Test
    public void unmatched() {
        TestFramework.assertFalse(src -> DexValueRef.$(src).matched());
    }
}
