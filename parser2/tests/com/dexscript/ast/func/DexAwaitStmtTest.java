package com.dexscript.ast.func;

import org.junit.Assert;
import org.junit.Test;

public class DexAwaitStmtTest {

    @Test
    public void empty() {
        Assert.assertEquals("await {}", new DexAwaitStmt("await {}").toString());
    }
}
