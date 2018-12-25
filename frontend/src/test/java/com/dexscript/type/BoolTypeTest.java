package com.dexscript.type;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BoolTypeTest {

    private TypeSystem ts;

    @Before
    public void setup() {
        ts = new TypeSystem();
    }

    @Test
    public void relationship() {
        Assert.assertEquals("bool", ts.BOOL.toString());
        Assert.assertEquals(ts.BOOL, new BoolType(ts));
        Assert.assertNotEquals(ts.BOOL, ts.STRING);
        TestAssignable.$(true, ts.BOOL, new BoolType(ts));
        TestAssignable.$(true, new BoolType(ts), ts.BOOL);
        TestAssignable.$(false, ts.BOOL, ts.STRING);
        TestAssignable.$(true, ts.BOOL, ts.literalOf(true));
        TestAssignable.$(true, ts.BOOL, ts.constOf(true));

        Assert.assertEquals("true", ts.literalOf(true).toString());
        Assert.assertEquals("false", ts.literalOf(false).toString());
        Assert.assertEquals(ts.literalOf(true), ts.literalOf(true));
        Assert.assertNotEquals(ts.literalOf(true), ts.literalOf(false));
        Assert.assertNotEquals(ts.literalOf(true), ts.STRING);
        TestAssignable.$(true, ts.literalOf(true), ts.literalOf(true));
        TestAssignable.$(false, ts.literalOf(true), ts.literalOf(false));
        TestAssignable.$(true, ts.literalOf(true), ts.constOf(true));
        TestAssignable.$(false, ts.literalOf(false), ts.constOf(true));

        Assert.assertEquals("(const)true", ts.constOf(true).toString());
        Assert.assertEquals("(const)false", ts.constOf(false).toString());
        Assert.assertEquals(ts.constOf(true), ts.constOf(true));
        Assert.assertNotEquals(ts.constOf(false), ts.constOf(true));
        TestAssignable.$(false, ts.constOf(true), ts.constOf(true));
        TestAssignable.$(false, ts.constOf(true), ts.literalOf(true));
    }

}
