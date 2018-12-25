package com.dexscript.ast.stmt;

import org.junit.Assert;
import org.junit.Test;

public class DexProduceStmtTest {

    @Test
    public void matched() {
        DexProduceStmt produceStmt = new DexProduceStmt("resolve 'hello' -> example");
        Assert.assertEquals("resolve 'hello' -> example", produceStmt.toString());
        Assert.assertEquals("'hello'", produceStmt.produced().toString());
        Assert.assertEquals("example", produceStmt.target().toString());
    }

    @Test
    public void no_space_after_produce_keyword() {
        DexProduceStmt produceStmt = new DexProduceStmt("resolve'hello' -> example");
        Assert.assertEquals("<unmatched>resolve'hello' -> example</unmatched>", produceStmt.toString());
    }

    @Test
    public void missing_produced_recover_by_blank() {
        DexProduceStmt produceStmt = new DexProduceStmt("resolve ?? -> example");
        Assert.assertEquals("resolve <error/>?? -> example", produceStmt.toString());
        Assert.assertEquals("<error/>", produceStmt.produced().toString());
        Assert.assertEquals("example", produceStmt.target().toString());
    }

    @Test
    public void missing_produced_recover_by_line_end() {
        DexProduceStmt produceStmt = new DexProduceStmt("resolve ; -> example");
        Assert.assertEquals("resolve <error/>", produceStmt.toString());
    }

    @Test
    public void missing_right_arrow_recover_by_blank() {
        DexProduceStmt produceStmt = new DexProduceStmt("resolve 'hello' ? example");
        Assert.assertEquals("resolve 'hello' <error/>? example", produceStmt.toString());
    }

    @Test
    public void missing_right_arrow_recover_by_line_end() {
        DexProduceStmt produceStmt = new DexProduceStmt("resolve 'hello' ?; example");
        Assert.assertEquals("resolve 'hello' <error/>?", produceStmt.toString());
    }

    @Test
    public void missing_target() {
        DexProduceStmt produceStmt = new DexProduceStmt("resolve 'hello' -> ??;example");
        Assert.assertEquals("resolve 'hello' -><error/> ??", produceStmt.toString());
        Assert.assertEquals("'hello'", produceStmt.produced().toString());
        Assert.assertEquals("<unmatched> ??;example</unmatched>", produceStmt.target().toString());
    }

    @Test
    public void produced_is_optional() {
        DexProduceStmt produceStmt = new DexProduceStmt("resolve-> example");
        Assert.assertEquals("resolve-> example", produceStmt.toString());
        Assert.assertNull(produceStmt.produced());
        Assert.assertEquals("example", produceStmt.target().toString());
        Assert.assertEquals("resolve -> example", new DexProduceStmt("resolve -> example").toString());
    }
}
