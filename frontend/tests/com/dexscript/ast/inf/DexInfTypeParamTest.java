package com.dexscript.ast.inf;

import org.junit.Assert;
import org.junit.Test;

public class DexInfTypeParamTest {

    @Test
    public void matched() {
        DexInfTypeParam typeArg = new DexInfTypeParam("<T>: string");
        Assert.assertEquals("<T>: string", typeArg.toString());
    }
}
