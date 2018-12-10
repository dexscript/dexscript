package com.dexscript.type;

import org.junit.Assert;
import org.junit.Test;

public class UInt8TypeTest {

    @Test
    public void uint8_is_assignable_from_uint8() {
        Assert.assertTrue(new UInt8Type().isAssignableFrom(BuiltinTypes.UINT8));
        Assert.assertTrue(BuiltinTypes.UINT8.isAssignableFrom(new UInt8Type()));
    }
}
