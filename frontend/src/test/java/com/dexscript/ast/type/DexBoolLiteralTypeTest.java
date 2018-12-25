package com.dexscript.ast.type;

import org.junit.Test;

import static com.dexscript.test.framework.TestFramework.testDataFrom;

public class DexBoolLiteralTypeTest {

    @Test
    public void matched() {
        testDataFrom(getClass()).assertMatched("Matched",
                text -> new DexBoolLiteralType(text).matched());
    }

    @Test
    public void unmatched() {
        testDataFrom(getClass()).assertNotMatched("Unmatched",
                text -> new DexBoolLiteralType(text).matched());
    }
}
