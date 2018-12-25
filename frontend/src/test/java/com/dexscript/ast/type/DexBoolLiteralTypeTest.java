package com.dexscript.ast.type;

import com.dexscript.test.framework.TestFramework;
import org.junit.Test;

public class DexBoolLiteralTypeTest {

    @Test
    public void matched() {
        TestFramework.assertMatched(text -> new DexBoolLiteralType(text).matched());
    }

    @Test
    public void unmatched() {
        TestFramework.assertNotMatched(text -> new DexBoolLiteralType(text).matched());
    }
}
