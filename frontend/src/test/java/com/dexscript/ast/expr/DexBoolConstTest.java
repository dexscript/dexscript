package com.dexscript.ast.expr;

import com.dexscript.test.framework.TestFramework;
import org.junit.Test;

public class DexBoolConstTest {

    @Test
    public void matched() {
        TestFramework.assertMatched(src -> DexBoolConst.$(src).matched());
    }

    @Test
    public void unmatched() {
        TestFramework.assertUnmatched(src -> DexBoolConst.$(src).matched());
    }
}
