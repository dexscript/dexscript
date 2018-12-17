package com.dexscript.type;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class Int64TypeTest {

    private TypeSystem ts;

    @Before
    public void setup() {
        ts = new TypeSystem();
    }

    @Test
    public void int64_is_assignable_from_int64() {
        Assert.assertTrue(IsAssignable.$(new Int64Type(ts), ts.INT64));
        Assert.assertTrue(IsAssignable.$(ts.INT64, new Int64Type(ts)));
    }

    @Test
    public void int64_is_assignable_from_in_range_int_literal() {
        Assert.assertTrue(IsAssignable.$(ts.INT64, new IntegerLiteralType(ts, "100")));
        Assert.assertFalse(IsAssignable.$(ts.INT64, new IntegerLiteralType(ts, "1" + Long.MAX_VALUE)));
    }
}
