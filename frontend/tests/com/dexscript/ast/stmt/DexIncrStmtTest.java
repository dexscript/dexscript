package com.dexscript.ast.stmt;

import org.junit.Assert;
import org.junit.Test;

public class DexIncrStmtTest {

    @Test
    public void matched() {
        Assert.assertEquals("i++", new DexIncrStmt("i++").toString());
    }
}
