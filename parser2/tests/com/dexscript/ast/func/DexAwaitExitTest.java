package com.dexscript.ast.func;

import org.junit.Assert;
import org.junit.Test;

public class DexAwaitExitTest {
    @Test
    public void matched() {
        Assert.assertEquals("  exit!", new DexAwaitExit("  exit! abc").toString());
    }

    @Test
    public void unmatched() {
        Assert.assertEquals("<unmatched>  exit !</unmatched>", new DexAwaitExit("  exit !").toString());
    }
}
