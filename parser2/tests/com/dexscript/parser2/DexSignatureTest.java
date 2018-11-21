package com.dexscript.parser2;

import org.junit.Assert;
import org.junit.Test;

public class DexSignatureTest {

    @Test
    public void one_param() {
        DexSignature sig = new DexSignature("(msg:string)");
        Assert.assertEquals("(msg:string)", sig.toString());
        Assert.assertEquals(1, sig.params().size());
        Assert.assertEquals("msg:string", sig.params().get(0).toString());
    }
}
