package com.dexscript.ast.type;

import org.junit.Assert;
import org.junit.Test;

public class DexStringLiteralTypeTest {

    @Test
    public void matched() {
        Assert.assertEquals("'hello'", new DexStringLiteralType("'hello'").toString());
    }
}
