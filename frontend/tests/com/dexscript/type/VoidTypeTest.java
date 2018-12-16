package com.dexscript.type;

import org.junit.Assert;
import org.junit.Test;

public class VoidTypeTest {

    @Test
    public void assignable_to_void() {
        TypeSystem ts = new TypeSystem();
        Assert.assertTrue(new VoidType(ts).isAssignableFrom(new VoidType(ts)));
    }
}
