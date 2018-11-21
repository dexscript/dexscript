package com.dexscript.parser2;

import org.junit.Assert;
import org.junit.Test;

public class DexStatementTest {

    @Test
    public void expression() {
        Assert.assertEquals("hello()", new DexStatement("hello()").exprStmt().toString());
    }
}
