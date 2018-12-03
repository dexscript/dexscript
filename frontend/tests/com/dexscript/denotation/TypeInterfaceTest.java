package com.dexscript.denotation;

import com.dexscript.ast.DexInterface;
import org.junit.Assert;
import org.junit.Test;

public class TypeInterfaceTest {

    @Test
    public void assignable_to_same_structure() {
        BuiltinTypes resolve = new BuiltinTypes();
        TypeInterface inf1 = new TypeInterface(resolve, new DexInterface("" +
                "interface Hello {\n" +
                "   Action1(): string\n" +
                "   Action2(): string\n" +
                "}"));
        TypeInterface inf2 = new TypeInterface(resolve, new DexInterface("" +
                "interface World {\n" +
                "   Action1(): string\n" +
                "   Action2(): string\n" +
                "}"));
        Assert.assertTrue(inf1.isAssignableFrom(inf2));
        Assert.assertTrue(inf2.isAssignableFrom(inf1));
    }

    @Test
    public void sub_type_has_more_member() {
        BuiltinTypes resolve = new BuiltinTypes();
        TypeInterface inf1 = new TypeInterface(resolve, new DexInterface("" +
                "interface Hello {\n" +
                "   Action1(): string\n" +
                "}"));
        TypeInterface inf2 = new TypeInterface(resolve, new DexInterface("" +
                "interface World {\n" +
                "   Action1(): string\n" +
                "   Action2(): string\n" +
                "}"));
        Assert.assertTrue(inf1.isAssignableFrom(inf2));
        Assert.assertFalse(inf2.isAssignableFrom(inf1));
    }
}
