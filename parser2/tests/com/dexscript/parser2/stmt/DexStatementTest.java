package com.dexscript.parser2.stmt;

import com.dexscript.parser2.expr.DexExpr;
import org.junit.Assert;
import org.junit.Test;

public class DexStatementTest {

    @Test
    public void expression() {
        Assert.assertEquals("hello()", ((DexExpr)DexStatement.parse("hello()")).toString());
    }

    @Test
    public void short_var_decl() {
        Assert.assertEquals("a:=b", ((DexShortVarDecl)DexStatement.parse("a:=b")).toString());
    }

    @Test
    public void block() {
        Assert.assertEquals("{}", ((DexBlock)DexStatement.parse("{}")).toString());
    }

    @Test
    public void stmt_in_block() {
        String src = "" +
                "{\n" +
                "   hello()\n" +
                "   world()\n" +
                "}";
        DexBlock blk = (DexBlock) DexStatement.parse(src);
        Assert.assertEquals(src, blk.toString());
        Assert.assertEquals(2, blk.stmts().size());
        Assert.assertEquals("hello()", blk.stmts().get(0).toString());
        Assert.assertEquals("world()", blk.stmts().get(1).toString());
    }
}
