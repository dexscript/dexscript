package com.dexscript.parser2.expr;

import org.junit.Assert;
import org.junit.Test;

public class DexStringLiteralTest {

    @Test
    public void matched() {
        Assert.assertEquals("'hello'", new DexStringLiteral("'hello'").toString());
    }
}
