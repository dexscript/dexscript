package com.dexscript.ast.type;

import com.dexscript.test.framework.TestFramework;
import org.junit.Test;

public class DexStringLiteralTypeTest {

    @Test
    public void matched() {
        TestFramework.assertTrue(text -> new DexStringLiteralType(text).matched());
    }

    @Test
    public void unmatched() {
        TestFramework.assertFalse(text -> new DexStringLiteralType(text).matched());
    }
}
