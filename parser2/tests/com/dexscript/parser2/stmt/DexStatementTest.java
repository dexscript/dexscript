package com.dexscript.parser2.stmt;

import org.junit.Assert;
import org.junit.Test;

public class DexStatementTest {

    @Test
    public void expression() {
        Assert.assertEquals("hello()", new DexStatement("hello()").exprStmt().toString());
    }

    @Test
    public void short_var_decl() {
        Assert.assertEquals("a:=b", new DexStatement("a:=b").shortVarDecl().toString());
    }

    @Test
    public void block() {
        Assert.assertEquals("{}", new DexStatement("{}").block().toString());
    }

    @Test
    public void stmt_in_block() {
        String src = "" +
                "{\n" +
                "   hello()\n" +
                "   world()\n" +
                "}";
        DexBlock blk = new DexStatement(src).block();
        Assert.assertEquals(src, blk.toString());
        Assert.assertEquals(2, blk.stmts().size());
        Assert.assertEquals("hello()", blk.stmts().get(0).toString());
        Assert.assertEquals("world()", blk.stmts().get(1).toString());
    }
}
