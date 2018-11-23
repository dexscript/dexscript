package com.dexscript.parser2.stmt;

import com.dexscript.parser2.core.DexElement;
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
        DexElement.Collector collector = new DexElement.Collector();
        DexBlock blk = new DexBlock("{return abc; return def}");
        blk.stmts().get(0).walkUp(collector);
        Assert.assertEquals("{return abc; return def}", collector.collected.get(0).toString());
        collector = new DexElement.Collector();
        blk.stmts().get(1).walkUp(collector);
        Assert.assertEquals("return abc", collector.collected.get(0).toString());
    }
}
