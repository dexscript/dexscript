package com.dexscript.type;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class IntegerTypeTest {

    private TypeSystem ts;

    @Before
    public void setup() {
        ts = new TypeSystem();
    }

    @Test
    public void relationship() {
        Assert.assertEquals("int64", ts.INT64.toString());
        Assert.assertEquals(new Int64Type(ts), ts.INT64);
        TestAssignable.$(true, new Int64Type(ts), ts.INT64);
        TestAssignable.$(true, ts.INT64, new Int64Type(ts));
        TestAssignable.$(true, ts.INT64, ts.literalOf(100));
        TestAssignable.$(true, ts.INT64, ts.constOf(100));

        Assert.assertEquals("int32", ts.INT32.toString());
        Assert.assertEquals(new Int32Type(ts), ts.INT32);
        TestAssignable.$(true, new Int32Type(ts), ts.INT32);
        TestAssignable.$(true, ts.INT32, new Int32Type(ts));
        TestAssignable.$(false, ts.INT32, ts.INT64);
        TestAssignable.$(false, ts.INT32, ts.literalOf(100));
        TestAssignable.$(true, ts.INT32, ts.constOf(100));

        Assert.assertEquals("100", ts.literalOf(100).toString());
        Assert.assertEquals(ts.literalOf(100), ts.literalOf(100));
        TestAssignable.$(true, ts.literalOf(100), ts.literalOf(100));
        TestAssignable.$(false, ts.literalOf(100), ts.INT64);
        TestAssignable.$(true, ts.literalOf(100), ts.constOf(100));
        TestAssignable.$(false, ts.literalOf(101), ts.constOf(100));

        Assert.assertEquals("(const)100", ts.constOf(100).toString());
        Assert.assertEquals(ts.constOf(100), ts.constOf(100));
        TestAssignable.$(false, ts.constOf(100), ts.INT32);
        TestAssignable.$(false, ts.constOf(100), ts.INT64);
    }
}
