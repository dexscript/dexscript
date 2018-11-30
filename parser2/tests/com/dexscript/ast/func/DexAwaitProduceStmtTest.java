package com.dexscript.ast.func;

import org.junit.Assert;
import org.junit.Test;

public class DexAwaitProduceStmtTest {

    @Test
    public void matched() {
        Assert.assertEquals("case AA() {}", new DexAwaitProduceStmt("case AA() {}").toString());
    }
}
