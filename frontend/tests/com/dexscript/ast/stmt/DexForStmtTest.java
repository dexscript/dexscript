package com.dexscript.ast.stmt;

import org.junit.Assert;
import org.junit.Test;

public class DexForStmtTest {

    @Test
    public void for_with_init_and_post() {
        Assert.assertEquals("for i := 0; i < 10; i++ {}",
                new DexForStmt("for i := 0; i < 10; i++ {}").toString());
    }
}
