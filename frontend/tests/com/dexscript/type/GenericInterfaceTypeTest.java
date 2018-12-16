package com.dexscript.type;

import com.dexscript.ast.DexInterface;
import com.dexscript.ast.core.DexSyntaxException;
import com.dexscript.ast.type.DexType;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class GenericInterfaceTypeTest {

    @Test
    public void resolve_with_assignable_type() {
        TypeSystem ts = new TypeSystem();
        ts.defineInterface(new DexInterface("" +
                "interface List {\n" +
                "   <T>: string\n" +
                "   Get__(index: int64): T\n" +
                "}"));
        DType inf1 = ts.resolveType("List", Arrays.asList(BuiltinTypes.STRING));
        Assert.assertNotEquals(BuiltinTypes.UNDEFINED, inf1);
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
        ts.resolveType("List", Arrays.asList(BuiltinTypes.INT64));
    }

    @Test
    public void resolve_generic_expansion_type() {
        TypeSystem ts = new TypeSystem();
        ts.defineInterface(new DexInterface("" +
                "interface List {\n" +
                "   <T>: string\n" +
                "   Get__(index: int64): T\n" +
                "}"));
        DType inf1 = ts.resolveType(DexType.parse("List<string>"));
        InterfaceType inf2 = ts.defineInterface(new DexInterface("" +
                "interface ListString {\n" +
                "   Get__(index: int64): string\n" +
                "}"));
        Assert.assertTrue(inf2.isAssignableFrom(inf1));
    }
}
