package com.dexscript.ast.stmt;

import org.junit.Assert;
import org.junit.Test;

public class DexAwaitExitTest {
    @Test
    public void matched() {
        Assert.assertEquals("  exit!", new DexAwaitExit("  exit! example").toString());
    }

    @Test
    public void unmatched() {
        Assert.assertEquals("<unmatched>  exit !</unmatched>", new DexAwaitExit("  exit !").toString());
    }
}
