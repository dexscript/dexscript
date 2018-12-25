package com.dexscript.ast.expr;

import org.junit.Assert;
import org.junit.Test;

public class DexNewExprTest {

    @Test
    public void zero_argument() {
        DexNewExpr newExpr = (DexNewExpr) DexExpr.$parse("new world()");
        Assert.assertEquals("new world()", newExpr.toString());
        Assert.assertEquals("world", newExpr.target().toString());
        Assert.assertEquals(0, newExpr.posArgs().size());
    }

    @Test
    public void missing_target() {
        DexNewExpr newExpr = (DexNewExpr) DexExpr.$parse("new ?()");
        Assert.assertEquals("new <error/>?()", newExpr.toString());
    }

    @Test
    public void three_arguments() {
        DexNewExpr newExpr = (DexNewExpr) DexExpr.$parse("new print(a1,b1,c1)");
        Assert.assertEquals("new print(a1,b1,c1)", newExpr.toString());
        Assert.assertEquals("print", newExpr.target().toString());
        Assert.assertEquals(3, newExpr.posArgs().size());
        Assert.assertEquals("a1", newExpr.posArgs().get(0).toString());
        Assert.assertEquals("b1", newExpr.posArgs().get(1).toString());
        Assert.assertEquals("c1", newExpr.posArgs().get(2).toString());
    }

    @Test
    public void missing_function_call() {
        DexNewExpr newExpr = (DexNewExpr) DexExpr.$parse("new print a1,b1,c1)");
        Assert.assertEquals("new print<error/> a1,b1,c1)", newExpr.toString());
    }
}
