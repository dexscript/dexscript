package com.dexscript.ast.inf;

import org.junit.Assert;
import org.junit.Test;

public class DexInfMethodTest {

    @Test
    public void matched() {
        Assert.assertEquals("Quack(): string", new DexInfMethod("Quack(): string").toString());
    }

    @Test
    public void invalid_identifier() {
        Assert.assertEquals("<unmatched>?(): string</unmatched>", new DexInfMethod("?(): string").toString());
    }

    @Test
    public void invalid_signature() {
        Assert.assertEquals("(<error/>: string", new DexInfMethod("Quack(: string").sig().toString());
    }
}
