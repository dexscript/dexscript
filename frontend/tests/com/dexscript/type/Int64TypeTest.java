package com.dexscript.type;

import org.junit.Assert;
import org.junit.Test;

public class Int64TypeTest {

    @Test
    public void int64_is_assignable_from_int64() {
        Assert.assertTrue(new Int64Type().isAssignableFrom(BuiltinTypes.INT64));
        Assert.assertTrue(BuiltinTypes.INT64.isAssignableFrom(new Int64Type()));
    }

    @Test
    public void int64_is_assignable_from_in_range_int_literal() {
        Assert.assertTrue(BuiltinTypes.INT64.isAssignableFrom(
                new IntegerLiteralType("100")));
        Assert.assertFalse(BuiltinTypes.INT64.isAssignableFrom(
                new IntegerLiteralType("1" + Long.MAX_VALUE)));
    }
}
