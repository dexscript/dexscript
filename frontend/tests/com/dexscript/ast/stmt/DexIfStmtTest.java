package com.dexscript.ast.stmt;

import org.junit.Assert;
import org.junit.Test;

public class DexIfStmtTest {
    @Test
    public void no_else() {
        Assert.assertEquals("if a==b {}", new DexIfStmt("if a==b {}").toString());
    }

    @Test
    public void with_else_if_only() {
        Assert.assertEquals("if a==b {} else if a==c {}",
                new DexIfStmt("if a==b {} else if a==c {}").toString());
    }

    @Test
    public void with_else_only() {
        Assert.assertEquals("if a==b {} else {}",
                new DexIfStmt("if a==b {} else {}").toString());
    }

    @Test
    public void with_else_if_and_else() {
        Assert.assertEquals("if a==b {} else if a==c {} else {}",
                new DexIfStmt("if a==b {} else if a==c {} else {}").toString());
    }

    @Test
    public void else_before_else_if() {
        Assert.assertEquals("if a==b {} else {}",
                new DexIfStmt("if a==b {} else {} else if a==c {}").toString());
    }
}
