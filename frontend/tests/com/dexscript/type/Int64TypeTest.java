package com.dexscript.type;

import org.junit.Assert;
import org.junit.Test;

public class Int64TypeTest {

    @Test
    public void int64_is_assignable_from_int64() {
        TypeSystem ts = new TypeSystem();
        Assert.assertTrue(new Int64Type(ts).isAssignableFrom(ts.INT64));
        Assert.assertTrue(ts.INT64.isAssignableFrom(new Int64Type(ts)));
    }

    @Test
    public void int64_is_assignable_from_in_range_int_literal() {
        TypeSystem ts = new TypeSystem();
        Assert.assertTrue(ts.INT64.isAssignableFrom(
                new IntegerLiteralType(ts, "100")));
        Assert.assertFalse(ts.INT64.isAssignableFrom(
                new IntegerLiteralType(ts, "1" + Long.MAX_VALUE)));
    }
}
