package com.dexscript.ast.type;

import org.junit.Assert;
import org.junit.Test;

public class DexGenericTypeTest {

    @Test
    public void matched() {
        DexType type = DexType.parse("Array<uint8>");
        Assert.assertEquals("Array<uint8>", type.toString());
    }
}
