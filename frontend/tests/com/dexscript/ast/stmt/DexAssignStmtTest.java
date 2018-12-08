package com.dexscript.ast.stmt;

import org.junit.Assert;
import org.junit.Test;

public class DexAssignStmtTest {

    @Test
    public void matched_one() {
        DexAssignStmt assignStmt = new DexAssignStmt("a=b");
        Assert.assertEquals("a=b", assignStmt.toString());
        Assert.assertEquals(1, assignStmt.targets().size());
        Assert.assertEquals("a", assignStmt.targets().get(0).toString());
        Assert.assertEquals("b", assignStmt.expr().toString());
    }

    @Test
    public void matched_two() {
        DexAssignStmt assignStmt = new DexAssignStmt("a,b=c");
        Assert.assertEquals("a,b=c", assignStmt.toString());
        Assert.assertEquals(2, assignStmt.targets().size());
        Assert.assertEquals("a", assignStmt.targets().get(0).toString());
        Assert.assertEquals("b", assignStmt.targets().get(1).toString());
        Assert.assertEquals("c", assignStmt.expr().toString());
    }

    @Test
    public void missing_expr() {
        Assert.assertEquals("a=<error/>??", new DexAssignStmt("a=??").toString());
    }
}
