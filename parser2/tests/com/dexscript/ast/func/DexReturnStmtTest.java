package com.dexscript.ast.func;

import org.junit.Assert;
import org.junit.Test;

public class DexReturnStmtTest {

    @Test
    public void matched() {
        Assert.assertEquals("return abc", new DexReturnStmt(" return abc").toString());
    }

    @Test
    public void garbage_in_prelude() {
        Assert.assertEquals("<unmatched>ddreturn abc</unmatched>", new DexReturnStmt("ddreturn abc").toString());
    }

    @Test
    public void missing_expr_recover_by_file_end() {
        Assert.assertEquals("return <error/>", new DexReturnStmt("return ").toString());
    }

    @Test
    public void missing_expr_recover_by_line_end() {
        Assert.assertEquals("return <error/>", new DexReturnStmt("return ; abc").toString());
    }

    @Test
    public void return_without_space() {
        Assert.assertEquals("<unmatched>returnabc</unmatched>", new DexReturnStmt("returnabc").toString());
    }

    @Test
    public void walk_up() {
        Assert.assertEquals("return abc", new DexReturnStmt("return abc").expr().prev().toString());
    }
}
