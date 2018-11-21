package com.dexscript.parser2;

import org.junit.Assert;
import org.junit.Test;

public class DexShortVarDeclTest {

    @Test
    public void matched() {
        Assert.assertEquals("a:=b", new DexShortVarDecl("a:=b").toString());
    }
}
