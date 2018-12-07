package com.dexscript.ast.func;

import org.junit.Assert;
import org.junit.Test;

public class DexProduceStmtTest {

    @Test
    public void matched() {
        DexProduceStmt produceStmt = new DexProduceStmt("produce 'hello' -> example");
        Assert.assertEquals("'hello'", produceStmt.produced().toString());
        Assert.assertEquals("example", produceStmt.target().toString());
    }
}
