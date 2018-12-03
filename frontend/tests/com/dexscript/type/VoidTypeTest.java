package com.dexscript.type;

import org.junit.Assert;
import org.junit.Test;

public class VoidTypeTest {

    @Test
    public void assignable_to_void() {
        Assert.assertTrue(new VoidType().isAssignableFrom(new VoidType()));
    }
}
