package com.dexscript.type;

import com.dexscript.ast.DexInterface;
import com.dexscript.ast.type.DexType;
import org.junit.Assert;
import org.junit.Test;

public class ArrayTypeTest {

    @Test
    public void resolve_array_type() {
        TypeSystem ts = new TypeSystem();
        DType type = ResolveType.$(ts, "uint8[]");
        InterfaceType inf = ts.defineInterface(new DexInterface("" +
                "interface GetSet {\n" +
                "   Get__(index: int64): uint8\n" +
                "   Set__(index: int64, value: uint8)\n" +
                "}"));
        Assert.assertTrue(inf.isAssignableFrom(type));
    }
}
