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
        ts.defineInterface(new DexInterface("" +
                "interface List {\n" +
                "   <T>: string\n" +
                "   Get__(index: int64): T\n" +
                "}"));
        DType inf1 = ResolveType.$(ts, "List<string>");
        Assert.assertNotEquals(ts.UNDEFINED, inf1);
        InterfaceType inf2 = ts.defineInterface(new DexInterface("" +
                "interface ListString {\n" +
                "   Get__(index: int64): string\n" +
                "}"));
        Assert.assertTrue(inf2.isAssignableFrom(inf1));
        Assert.assertTrue(inf1.isAssignableFrom(inf2));
        InterfaceType inf3 = ts.defineInterface(new DexInterface("" +
                "interface ListInt {\n" +
                "   Get__(index: int64): int64\n" +
                "}"));
        Assert.assertFalse(inf3.isAssignableFrom(inf1));
    }

    @Test(expected = DexSyntaxException.class)
    public void resolve_with_not_assignable_type() {
        TypeSystem ts = new TypeSystem();
        ts.defineInterface(new DexInterface("" +
                "interface List {\n" +
                "   <T>: string\n" +
                "   Get__(index: int64): T\n" +
                "}"));
        ResolveType.$(ts, "List<int64>");
    }

    @Test
    public void resolve_generic_expansion_type() {
        TypeSystem ts = new TypeSystem();
        ts.defineInterface(new DexInterface("" +
                "interface List {\n" +
                "   <T>: string\n" +
                "   Get__(index: int64): T\n" +
                "}"));
        DType inf1 = ResolveType.$(ts, "List<string>");
        InterfaceType inf2 = ts.defineInterface(new DexInterface("" +
                "interface ListString {\n" +
                "   Get__(index: int64): string\n" +
                "}"));
        Assert.assertTrue(inf2.isAssignableFrom(inf1));
    }
}
