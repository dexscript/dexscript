package com.dexscript.ast.func;

import org.junit.Assert;
import org.junit.Test;

public class DexReturnStmtTest {

    @Test
    public void matched() {
        Assert.assertEquals("return example", new DexReturnStmt(" return example").toString());
    }

    @Test
    public void garbage_in_prelude() {
        Assert.assertEquals("<unmatched>ddreturn example</unmatched>", new DexReturnStmt("ddreturn example").toString());
    }

    @Test
    public void missing_expr_recover_by_file_end() {
        Assert.assertEquals("return <error/>", new DexReturnStmt("return ").toString());
    }

    @Test
    public void missing_expr_recover_by_line_end() {
        Assert.assertEquals("return <error/>", new DexReturnStmt("return ; example").toString());
    }

    @Test
    public void return_without_space() {
        Assert.assertEquals("<unmatched>returnabc</unmatched>", new DexReturnStmt("returnabc").toString());
    }

    @Test
    public void walk_up() {
        Assert.assertEquals("return example", new DexReturnStmt("return example").expr().prev().toString());
    }
}
