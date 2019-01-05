package com.dexscript.type.core;

import com.dexscript.type.core.AnyType;
import com.dexscript.type.core.IsAssignable;
import com.dexscript.type.core.ResolveType;
import com.dexscript.type.core.TypeSystem;
import org.junit.Assert;
import org.junit.Test;

public class AnyTypeTest {

    @Test
    public void empty_interface_is_assignable_from_anything() {
        TypeSystem ts = new TypeSystem();
        AnyType type = (AnyType) ResolveType.$(ts, "interface {}");
        Assert.assertTrue(IsAssignable.$(type, ts.STRING));
        Assert.assertTrue(IsAssignable.$(type, ts.INT64));
    }
}
