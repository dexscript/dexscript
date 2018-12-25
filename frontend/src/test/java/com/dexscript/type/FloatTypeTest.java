package com.dexscript.type;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class FloatTypeTest {

    private TypeSystem ts;

    @Before
    public void setup() {
        ts = new TypeSystem();
    }

    @Test
    public void relationship() {
        Assert.assertEquals("float64", ts.FLOAT64.toString());
        Assert.assertEquals(ts.FLOAT64, new Float64Type(ts));
        TestAssignable.$(true, new Float64Type(ts), ts.FLOAT64);
        TestAssignable.$(false, ts.FLOAT64, ts.FLOAT32);
        TestAssignable.$(true, ts.FLOAT64, ts.constOf(100.0));
        TestAssignable.$(true, ts.FLOAT64, ts.constOf(100));

        Assert.assertEquals("float32", ts.FLOAT32.toString());
        Assert.assertEquals(ts.FLOAT32, new Float32Type(ts));
        TestAssignable.$(true, new Float32Type(ts), ts.FLOAT32);
        TestAssignable.$(false, ts.FLOAT32, ts.FLOAT64);
        TestAssignable.$(true, ts.FLOAT32, ts.constOf(100.0));
        TestAssignable.$(true, ts.FLOAT32, ts.constOf(100));

        Assert.assertEquals("(const)100.0", ts.constOf(100.0).toString());
        Assert.assertEquals(ts.constOf(100.0), ts.constOf(100.0));
        TestAssignable.$(false, ts.constOf(100.0), ts.FLOAT32);
        TestAssignable.$(false, ts.constOf(100.0), ts.FLOAT64);
    }
}
