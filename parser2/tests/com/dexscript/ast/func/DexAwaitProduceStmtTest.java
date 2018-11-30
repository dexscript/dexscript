package com.dexscript.ast.func;

import org.junit.Assert;
import org.junit.Test;

public class DexAwaitProduceStmtTest {

    @Test
    public void matched() {
        Assert.assertEquals("case AA() {}", new DexAwaitProduceStmt("case AA() {}").toString());
    }

    @Test
    public void invalid_identifier() {
        Assert.assertEquals("<unmatched>case ??() {}</unmatched>", new DexAwaitProduceStmt("case ??() {}").toString());
    }

    @Test
    public void missing_blank() {
        Assert.assertEquals("<unmatched>caseAA() {}</unmatched>", new DexAwaitProduceStmt("caseAA() {}").toString());
    }

    @Test
    public void missing_left_paren() {
        Assert.assertEquals("<unmatched>case AA) {}</unmatched>", new DexAwaitProduceStmt("case AA) {}").toString());
    }

    @Test
    public void missing_right_paren() {
        Assert.assertEquals("case AA( {}<error/>", new DexAwaitProduceStmt("case AA( {}").toString());
    }

    @Test
    public void missing_block() {
        Assert.assertEquals("case AA() {", new DexAwaitProduceStmt("case AA() {").toString());
    }
}
