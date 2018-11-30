package com.dexscript.ast.func;

import org.junit.Assert;
import org.junit.Test;

public class DexAwaitProducerStmtTest {

    @Test
    public void matched() {
        DexAwaitProducerStmt stmt = new DexAwaitProducerStmt("case <-abc {}");
        Assert.assertEquals("case <-abc {}", stmt.toString());
    }

    @Test
    public void no_consume_statement() {
        DexAwaitProducerStmt stmt = new DexAwaitProducerStmt("case 1+1 {}");
        Assert.assertEquals("<unmatched>case 1+1 {}</unmatched>", stmt.toString());
    }

    @Test
    public void missing_left_brace() {
        DexAwaitProducerStmt stmt = new DexAwaitProducerStmt("case <-abc }");
        Assert.assertEquals("<unmatched>case <-abc }</unmatched>", stmt.toString());
    }

    @Test
    public void missing_right_brace() {
        DexAwaitProducerStmt stmt = new DexAwaitProducerStmt("case <-abc {");
        Assert.assertEquals("case <-abc {", stmt.toString());
        Assert.assertEquals("{<error/>", stmt.blk().toString());
    }
}
