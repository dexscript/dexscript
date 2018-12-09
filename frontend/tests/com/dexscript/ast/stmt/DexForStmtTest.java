package com.dexscript.ast.stmt;

import org.junit.Assert;
import org.junit.Test;

public class DexForStmtTest {

    @Test
    public void forever() {
        DexForStmt forStmt = new DexForStmt("for {}");
        Assert.assertEquals("for {}", forStmt.toString());
        Assert.assertTrue(forStmt.isForever());
    }

    @Test
    public void for_condition() {
        DexForStmt forStmt = new DexForStmt("for a==1 {}");
        Assert.assertEquals("for a==1 {}", forStmt.toString());
        Assert.assertTrue(forStmt.isForCondition());
        Assert.assertEquals("a==1", forStmt.condition().toString());
    }

    @Test
    public void for_with_init_and_post() {
        DexForStmt forStmt = new DexForStmt("for i := 0; i < 10; i++ {}");
        Assert.assertEquals("for i := 0; i < 10; i++ {}", forStmt.toString());
        Assert.assertTrue(forStmt.isForWith3Clauses());
        Assert.assertEquals("i := 0", forStmt.initStmt().toString());
        Assert.assertEquals("i < 10", forStmt.condition().toString());
        Assert.assertEquals("i++", forStmt.postStmt().toString());
    }

    @Test
    public void init_is_optional() {
        DexForStmt forStmt = new DexForStmt("for ; i < 10; i++ {}");
        Assert.assertEquals("for ; i < 10; i++ {}", forStmt.toString());
        Assert.assertTrue(forStmt.isForWith3Clauses());
        Assert.assertEquals("i < 10", forStmt.condition().toString());
        Assert.assertEquals("i++", forStmt.postStmt().toString());
    }

    @Test
    public void post_is_optional() {
        DexForStmt forStmt = new DexForStmt("for i := 0 ; i < 10; {}");
        Assert.assertEquals("for i := 0 ; i < 10; {}", forStmt.toString());
        Assert.assertTrue(forStmt.isForWith3Clauses());
        Assert.assertEquals("i := 0", forStmt.initStmt().toString());
        Assert.assertEquals("i < 10", forStmt.condition().toString());
    }

    @Test
    public void init_and_post_is_optional() {
        DexForStmt forStmt = new DexForStmt("for ; i < 10; {}");
        Assert.assertEquals("for ; i < 10; {}", forStmt.toString());
        Assert.assertTrue(forStmt.isForCondition());
        Assert.assertEquals("i < 10", forStmt.condition().toString());
    }

    @Test
    public void for_with_3_semi_colon() {
        DexForStmt forStmt = new DexForStmt("for ;; {}");
        Assert.assertTrue(forStmt.isForever());
    }
}
