package com.dexscript.ast.type;

import com.dexscript.test.framework.TestFramework;
import org.junit.Test;

public class DexIntegerLiteralTypeTest {

    @Test
    public void matched() {
        TestFramework.assertMatched(text -> DexIntegerLiteralType.$(text).matched());
    }

    @Test
    public void unmatched() {
        TestFramework.assertNotMatched(text -> DexIntegerLiteralType.$(text).matched());
    }
}
