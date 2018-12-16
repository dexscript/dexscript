package com.dexscript.type;

import org.junit.Assert;
import org.junit.Test;

public class UInt8TypeTest {

    @Test
    public void uint8_is_assignable_from_uint8() {
        TypeSystem ts = new TypeSystem();
        Assert.assertTrue(new UInt8Type(ts).isAssignableFrom(ts.UINT8));
        Assert.assertTrue(ts.UINT8.isAssignableFrom(new UInt8Type(ts)));
    }
}
