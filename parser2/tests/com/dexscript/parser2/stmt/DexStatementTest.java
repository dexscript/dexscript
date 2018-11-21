package com.dexscript.parser2.stmt;

import com.dexscript.parser2.stmt.DexStatement;
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
}
