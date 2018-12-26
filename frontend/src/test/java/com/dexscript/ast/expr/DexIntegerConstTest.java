package com.dexscript.ast.expr;

import com.dexscript.test.framework.TestFramework;
import org.junit.Test;

public class DexIntegerConstTest {

    @Test
    public void matched() {
        TestFramework.assertMatched(src -> DexIntegerConst.$(src).matched());
    }

    @Test
    public void unmatched() {
        TestFramework.assertUnmatched(src -> DexIntegerConst.$(src).matched());
    }
}
