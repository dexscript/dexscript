package com.dexscript.ast.type;

import com.dexscript.ast.core.Text;
import org.junit.Assert;
import org.junit.Test;

public class DexVoidTypeTest {

    @Test
    public void matched() {
        Assert.assertEquals("void", new DexVoidType("void").toString());
    }

    @Test
    public void unmatched() {
        Assert.assertEquals("<unmatched>void2</unmatched>", new DexVoidType("void2").toString());
    }

    @Test
    public void force_matched() {
        Assert.assertEquals("void", new DexVoidType(new Text("xxxx"), true).toString());
    }
}
