package com.dexscript.type;

import org.junit.Assert;
import org.junit.Test;

public class BoolTypeTest {

    @Test
    public void is_assignable() {
        TypeSystem ts = new TypeSystem();
        Assert.assertTrue(IsAssignable.$(ts.BOOL, new BoolType(ts)));
        Assert.assertTrue(IsAssignable.$(new BoolType(ts), ts.BOOL));
    }
}
