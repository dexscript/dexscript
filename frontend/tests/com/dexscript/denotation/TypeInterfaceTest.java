package com.dexscript.denotation;

import com.dexscript.ast.DexInterface;
import org.junit.Assert;
import org.junit.Test;

public class TypeInterfaceTest {

    @Test
    public void assignable_to_same_structure() {
        TypeInterface inf1 = new TypeInterface(BuiltinTypes.TYPE_TABLE, new DexInterface("" +
                "interface Hello {\n" +
                "   Action1(): string\n" +
                "   Action2(): string\n" +
                "}"));
        TypeInterface inf2 = new TypeInterface(BuiltinTypes.TYPE_TABLE, new DexInterface("" +
                "interface World {\n" +
                "   Action1(): string\n" +
                "   Action2(): string\n" +
                "}"));
        Assert.assertTrue(inf1.isAssignableFrom(inf2));
        Assert.assertTrue(inf2.isAssignableFrom(inf1));
    }

    @Test
    public void sub_type_has_more_member() {
        TypeInterface inf1 = new TypeInterface(BuiltinTypes.TYPE_TABLE, new DexInterface("" +
                "interface Hello {\n" +
                "   Action1(): string\n" +
                "}"));
        TypeInterface inf2 = new TypeInterface(BuiltinTypes.TYPE_TABLE, new DexInterface("" +
                "interface World {\n" +
                "   Action1(): string\n" +
                "   Action2(): string\n" +
                "}"));
        Assert.assertTrue(inf1.isAssignableFrom(inf2));
        Assert.assertFalse(inf2.isAssignableFrom(inf1));
    }

    @Test
    public void argument_takes_sub_type() {
        TopLevelTypeTable resolve = new TopLevelTypeTable(BuiltinTypes.TYPE_TABLE);
        resolve.add(new TypeInterface(resolve, new DexInterface("" +
                "interface SuperType {\n" +
                "   Action1(): string\n" +
                "}")));
        resolve.add(new TypeInterface(resolve, new DexInterface("" +
                "interface SubType {\n" +
                "   Action1(): string\n" +
                "   Action2(): string\n" +
                "}")));
        TypeInterface inf1 = new TypeInterface(resolve, new DexInterface("" +
                "interface Hello {\n" +
                "   Action(arg: SuperType): string\n" +
                "}"));
        TypeInterface inf2 = new TypeInterface(resolve, new DexInterface("" +
                "interface World {\n" +
                "   Action(arg: SubType): string\n" +
                "}"));
        Assert.assertTrue(inf1.isAssignableFrom(inf2));
        Assert.assertFalse(inf2.isAssignableFrom(inf1));
    }

    @Test
    public void ret_takes_sub_type() {

        TopLevelTypeTable resolve = new TopLevelTypeTable(BuiltinTypes.TYPE_TABLE);
        resolve.add(new TypeInterface(resolve, new DexInterface("" +
                "interface SuperType {\n" +
                "   Action1(): string\n" +
                "}")));
        resolve.add(new TypeInterface(resolve, new DexInterface("" +
                "interface SubType {\n" +
                "   Action1(): string\n" +
                "   Action2(): string\n" +
                "}")));
        TypeInterface inf1 = new TypeInterface(resolve, new DexInterface("" +
                "interface Hello {\n" +
                "   Action(): SuperType\n" +
                "}"));
        TypeInterface inf2 = new TypeInterface(resolve, new DexInterface("" +
                "interface World {\n" +
                "   Action(): SubType\n" +
                "}"));
        Assert.assertTrue(inf1.isAssignableFrom(inf2));
        Assert.assertFalse(inf2.isAssignableFrom(inf1));
    }
}
