package com.dexscript.type;

import org.junit.Assert;
import org.junit.Test;

public class AnyTypeTest {

    @Test
    public void resolve_empty_interface() {
        TypeSystem ts = new TypeSystem();
        AnyType type = (AnyType) ResolveType.$(ts, "interface {}");
        Assert.assertTrue(IsAssignable.$(type, ts.STRING));
        Assert.assertTrue(IsAssignable.$(type, ts.INT64));
    }
}
