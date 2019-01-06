package com.dexscript.type.core;

import com.dexscript.ast.type.DexType;
import org.junit.Assert;
import org.junit.Test;

public class AnyTypeTest {

    @Test
    public void empty_interface_is_assignable_from_anything() {
        TypeSystem ts = new TypeSystem();
        AnyType type = (AnyType) InferType.$(ts, DexType.$parse("interface {}"));
        Assert.assertTrue(IsAssignable.$(type, ts.STRING));
        Assert.assertTrue(IsAssignable.$(type, ts.INT64));
    }
}
