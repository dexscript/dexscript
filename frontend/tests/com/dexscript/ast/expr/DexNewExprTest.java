package com.dexscript.ast.expr;

import org.junit.Assert;
import org.junit.Test;

public class DexNewExprTest {

    @Test
    public void zero_argument() {
        DexNewExpr newExpr = (DexNewExpr) DexExpr.parse("new world()");
        Assert.assertEquals("new world()", newExpr.toString());
        Assert.assertEquals("world", newExpr.target().toString());
        Assert.assertEquals(0, newExpr.args().size());
    }

    @Test
    public void new_target_with_paren() {
        DexNewExpr newExpr = (DexNewExpr) DexExpr.parse("new (world)()");
        Assert.assertEquals("new (world)()", newExpr.toString());
    }

    @Test
    public void missing_target() {
        DexNewExpr newExpr = (DexNewExpr) DexExpr.parse("new ?()");
        Assert.assertEquals("new <error/>?()", newExpr.toString());
    }

    @Test
    public void three_arguments() {
        DexNewExpr newExpr = (DexNewExpr) DexExpr.parse("new print(a1,b1,c1)");
        Assert.assertEquals("new print(a1,b1,c1)", newExpr.toString());
        Assert.assertEquals("print", newExpr.target().toString());
        Assert.assertEquals(3, newExpr.args().size());
        Assert.assertEquals("a1", newExpr.args().get(0).toString());
        Assert.assertEquals("b1", newExpr.args().get(1).toString());
        Assert.assertEquals("c1", newExpr.args().get(2).toString());
    }

    @Test
    public void missing_argument() {
        DexNewExpr newExpr = (DexNewExpr) DexExpr.parse("new print(?,?,c1)");
        Assert.assertEquals("new print(<error/>?,?,c1)", newExpr.toString());
        Assert.assertEquals("print", newExpr.target().toString());
        Assert.assertEquals(3, newExpr.args().size());
        Assert.assertEquals("<error/>", newExpr.args().get(0).toString());
        Assert.assertEquals("<error/>", newExpr.args().get(1).toString());
        Assert.assertEquals("c1", newExpr.args().get(2).toString());
    }

    @Test
    public void one_argument() {
        DexNewExpr newExpr = (DexNewExpr) DexExpr.parse("new print(a)");
        Assert.assertEquals("new print(a)", newExpr.toString());
        Assert.assertEquals("print", newExpr.target().toString());
        Assert.assertEquals(1, newExpr.args().size());
        Assert.assertEquals("a", newExpr.args().get(0).toString());
    }
}
