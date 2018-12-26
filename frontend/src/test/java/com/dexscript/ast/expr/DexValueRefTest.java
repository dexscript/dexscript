package com.dexscript.ast.expr;

import com.dexscript.test.framework.TestFramework;
import org.junit.Assert;
import org.junit.Test;

public class DexValueRefTest {

    @Test
    public void matched() {
        TestFramework.assertMatched(src -> DexValueRef.$(src).matched());
    }

    @Test
    public void unmatched() {
        TestFramework.assertUnmatched(src -> DexValueRef.$(src).matched());
    }
}
