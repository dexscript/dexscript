package com.dexscript.ast.func;

import org.junit.Assert;
import org.junit.Test;

public class DexBlockTest {

    @Test
    public void empty() {
        Assert.assertEquals("{}", new DexBlock("{}").toString());
    }

    @Test
    public void one_statement() {
        DexBlock blk = new DexBlock("{hello()}");
        Assert.assertEquals("{hello()}", blk.toString());
        Assert.assertEquals(1, blk.stmts().size());
        Assert.assertEquals("hello()", blk.stmts().get(0).toString());
    }

    @Test
    public void statements_separated_by_new_line() {
        DexBlock blk = new DexBlock("{hello()\nworld()}");
        Assert.assertEquals("{hello()\nworld()}", blk.toString());
        Assert.assertEquals(2, blk.stmts().size());
        Assert.assertEquals("hello()", blk.stmts().get(0).toString());
        Assert.assertEquals("world()", blk.stmts().get(1).toString());
    }

    @Test
    public void statements_separated_by_semicolon() {
        DexBlock blk = new DexBlock("{hello();world()}");
        Assert.assertEquals("{hello();world()}", blk.toString());
        Assert.assertEquals(2, blk.stmts().size());
        Assert.assertEquals("hello()", blk.stmts().get(0).toString());
        Assert.assertEquals("world()", blk.stmts().get(1).toString());
    }

    @Test
    public void walk_up() {
        DexBlock blk = new DexBlock("{return example; return def}");
        Assert.assertEquals("{return example; return def}", blk.stmts().get(0).prev().toString());
        Assert.assertEquals("return example", blk.stmts().get(1).prev().toString());
    }

    @Test
    public void recover_from_invalid_statement_by_line_end() {
        String src = "" +
                "{\n" +
                "??\n" +
                "return example\n" +
                "}";
        DexBlock blk = new DexBlock(src);
        Assert.assertEquals("{\n" +
                "<error/>??\n" +
                "return example\n" +
                "}", blk.toString());
        Assert.assertEquals("<error/>", blk.stmts().get(0).toString());
        Assert.assertEquals("return example", blk.stmts().get(1).toString());
    }

    @Test
    public void recover_from_last_invalid_statement() {
        String src = "" +
                "{\n" +
                "return example;??" +
                "}xyz";
        DexBlock blk = new DexBlock(src);
        Assert.assertEquals("{\n" +
                "return example;<error/>??}", blk.toString());
        Assert.assertEquals("return example", blk.stmts().get(0).toString());
        Assert.assertEquals("<error/>", blk.stmts().get(1).toString());
    }
}
