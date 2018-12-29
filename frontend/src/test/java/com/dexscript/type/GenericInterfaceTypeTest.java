package com.dexscript.type;

import com.dexscript.ast.DexInterface;
import com.dexscript.ast.core.DexSyntaxException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class GenericInterfaceTypeTest {

    private TypeSystem ts;

    @Before
    public void setup() {
        ts = new TypeSystem();
    }

    @Test
    public void resolve_with_assignable_type() {
        ts.defineInterface(DexInterface.$("" +
                "interface List {\n" +
                "   <T>: string\n" +
                "   Get__(index: int64): T\n" +
                "}"));
        DType inf1 = ResolveType.$(ts, "List<string>");
        Assert.assertNotEquals(ts.UNDEFINED, inf1);
        InterfaceType inf2 = ts.defineInterface(DexInterface.$("" +
                "interface ListString {\n" +
                "   Get__(index: int64): string\n" +
                "}"));

        Assert.assertTrue(IsAssignable.$(inf2, inf1));
        Assert.assertTrue(IsAssignable.$(inf1, inf2));
        InterfaceType inf3 = ts.defineInterface(DexInterface.$("" +
                "interface ListInt {\n" +
                "   Get__(index: int64): int64\n" +
                "}"));
        Assert.assertFalse(IsAssignable.$(inf3, inf1));
    }

    @Test
    public void resolve_with_not_assignable_type() {
        ts.defineInterface(DexInterface.$("" +
                "interface List {\n" +
                "   <T>: string\n" +
                "   Get__(index: int64): T\n" +
                "}"));
        Assert.assertEquals(ts.UNDEFINED, ResolveType.$(ts, "List<int64>"));
    }

    @Test
    public void resolve_generic_expansion_type() {
        ts.defineInterface(DexInterface.$("" +
                "interface List {\n" +
                "   <T>: string\n" +
                "   Get__(index: int64): T\n" +
                "}"));
        DType inf1 = ResolveType.$(ts, "List<string>");
        InterfaceType inf2 = ts.defineInterface(DexInterface.$("" +
                "interface ListString {\n" +
                "   Get__(index: int64): string\n" +
                "}"));
        Assert.assertTrue(IsAssignable.$(inf2, inf1));
    }
}
