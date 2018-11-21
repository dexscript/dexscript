package com.dexscript.parser2;

import org.junit.Assert;
import org.junit.Test;

public class DexBlockTest {

    @Test
    public void empty() {
        Assert.assertEquals("{}", new DexBlock("{}").toString());
    }
}
