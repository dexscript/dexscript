package com.dexscript.type;

import com.dexscript.ast.type.DexInterfaceType;
import org.junit.Assert;
import org.junit.Test;

public class AnyTypeTest {

    @Test
    public void resolve_empty_interface() {
        TypeSystem ts = new TypeSystem();
        AnyType type = (AnyType) ts.resolveType(new DexInterfaceType("interface {}"));
        Assert.assertTrue(type.isAssignableFrom(BuiltinTypes.STRING));
        Assert.assertTrue(type.isAssignableFrom(BuiltinTypes.INT64));
    }
}
