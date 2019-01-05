package com.dexscript.type.core;

import com.dexscript.type.core.IsAssignable;
import com.dexscript.type.core.TypeSystem;
import com.dexscript.type.core.UInt8Type;
import org.junit.Assert;
import org.junit.Test;

public class UInt8TypeTest {

    @Test
    public void uint8_is_assignable_from_uint8() {
        TypeSystem ts = new TypeSystem();
        Assert.assertTrue(IsAssignable.$(new UInt8Type(ts), ts.UINT8));
        Assert.assertTrue(IsAssignable.$(ts.UINT8, new UInt8Type(ts)));
    }
}
