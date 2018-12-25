package com.dexscript.ast.type;

import org.junit.Assert;
import org.junit.Test;

public class DexIntegerLiteralTypeTest {

    @Test
    public void matched() {
        Assert.assertEquals("1", new DexIntegerLiteralType("1").toString());
    }
}
