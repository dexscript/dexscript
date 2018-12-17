package com.dexscript.type;

import org.junit.Assert;
import org.junit.Test;

public class VoidTypeTest {

    @Test
    public void assignable_to_void() {
        TypeSystem ts = new TypeSystem();
        Assert.assertTrue(IsAssignable.$(new VoidType(ts), ts.VOID));
    }
}
