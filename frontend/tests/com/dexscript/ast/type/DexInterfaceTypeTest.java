package com.dexscript.ast.type;

import org.junit.Assert;
import org.junit.Test;

public class DexInterfaceTypeTest {

    @Test
    public void matched() {
        DexInterfaceType inf = new DexInterfaceType("interface {}");
        Assert.assertEquals("interface {}", inf.toString());
    }
}
