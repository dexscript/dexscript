package com.dexscript.ast.func;

import org.junit.Assert;
import org.junit.Test;

public class DexAwaitConsumerTest {

    @Test
    public void matched() {
        DexAwaitConsumer stmt = new DexAwaitConsumer("case AA() {}");
        Assert.assertEquals("case AA() {}", stmt.toString());
        Assert.assertEquals(stmt, stmt.produceSig().parent());
        Assert.assertEquals(stmt, stmt.blk().parent());
    }

    @Test
    public void invalid_identifier() {
        Assert.assertEquals("<unmatched>case ??() {}</unmatched>", new DexAwaitConsumer("case ??() {}").toString());
    }

    @Test
    public void missing_blank() {
        Assert.assertEquals("<unmatched>caseAA() {}</unmatched>", new DexAwaitConsumer("caseAA() {}").toString());
    }

    @Test
    public void missing_left_paren() {
        Assert.assertEquals("<unmatched>case AA) {}</unmatched>", new DexAwaitConsumer("case AA) {}").toString());
    }

    @Test
    public void missing_right_paren() {
        Assert.assertEquals("case AA( {}<error/>", new DexAwaitConsumer("case AA( {}").toString());
    }

    @Test
    public void missing_block() {
        Assert.assertEquals("case AA() {", new DexAwaitConsumer("case AA() {").toString());
    }
}
