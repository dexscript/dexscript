package com.dexscript.ast.type;

import org.junit.Assert;
import org.junit.Test;

public class DexTypeArrayTest {

    @Test
    public void matched() {
        DexType byteArray = DexType.parse("byte[]");
        Assert.assertEquals("byte[]", byteArray.toString());
    }
}
