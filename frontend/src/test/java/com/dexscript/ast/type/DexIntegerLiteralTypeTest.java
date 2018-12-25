package com.dexscript.ast.type;

import org.junit.Test;

import static com.dexscript.test.framework.TestFramework.testDataFrom;

public class DexIntegerLiteralTypeTest {

    @Test
    public void matched() {
        testDataFrom(getClass()).assertMatched("Matched",
                text -> DexIntegerLiteralType.$(text).matched());
    }

    @Test
    public void unmatched() {
        testDataFrom(getClass()).assertNotMatched("Unmatched",
                text -> DexIntegerLiteralType.$(text).matched());
    }
}
