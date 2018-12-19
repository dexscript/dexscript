package com.dexscript.ast.expr;

import org.junit.Assert;
import org.junit.Test;

public class DexArrayLiteralTest {

    @Test
    public void matched() {
        Assert.assertEquals("[]", new DexArrayLiteral("[]").toString());
    }
}
