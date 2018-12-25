package com.dexscript.ast.stmt;

import org.junit.Assert;
import org.junit.Test;

public class DexAwaitProducerTest {

    @Test
    public void matched() {
        DexAwaitProducer stmt = new DexAwaitProducer("case <-example {}");
        Assert.assertEquals("case <-example {}", stmt.toString());
        Assert.assertEquals(stmt, stmt.blk().parent());
        Assert.assertEquals(stmt, stmt.consumeStmt().parent());
    }

    @Test
    public void missing_left_brace() {
        DexAwaitProducer stmt = new DexAwaitProducer("case <-example }");
        Assert.assertEquals("<unmatched>case <-example }</unmatched>", stmt.toString());
    }

    @Test
    public void missing_right_brace() {
        DexAwaitProducer stmt = new DexAwaitProducer("case <-example {");
        Assert.assertEquals("case <-example {", stmt.toString());
        Assert.assertEquals("{<error/>", stmt.blk().toString());
    }
}
