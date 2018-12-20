package com.dexscript.ast.type;

import org.junit.Assert;
import org.junit.Test;

public class DexBoolLiteralTypeTest {

    @Test
    public void matched() {
        Assert.assertEquals("true", new DexBoolLiteralType("true").toString());
    }
}
