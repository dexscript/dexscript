package com.dexscript.ast.type;

import com.dexscript.test.framework.TestFramework;
import org.junit.Test;

public class DexIntegerLiteralTypeTest {

    @Test
    public void matched() {
        TestFramework.assertTrue(text -> DexIntegerLiteralType.$(text).matched());
    }

    @Test
    public void unmatched() {
        TestFramework.assertFalse(text -> DexIntegerLiteralType.$(text).matched());
    }
}
