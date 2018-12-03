package com.dexscript.denotation;

import com.dexscript.ast.DexInterface;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class TypeInterfaceTest {

    @Test
    public void assignable_to_same_structure() {
        FunctionTable functionTable = new FunctionTable();
        TopLevelTypeTable typeTable = new TopLevelTypeTable(BuiltinTypes.TYPE_TABLE);
        TypeInterface inf1 = new TypeInterface(typeTable, functionTable, new DexInterface("" +
                "interface Hello {\n" +
                "   Action1(): string\n" +
                "   Action2(): string\n" +
                "}"));
        TypeInterface inf2 = new TypeInterface(typeTable, functionTable, new DexInterface("" +
                "interface World {\n" +
                "   Action1(): string\n" +
                "   Action2(): string\n" +
                "}"));
        Assert.assertTrue(inf1.isAssignableFrom(inf2));
        Assert.assertTrue(inf2.isAssignableFrom(inf1));
    }

    @Test
    public void sub_type_has_more_member() {
        FunctionTable functionTable = new FunctionTable();
        TopLevelTypeTable typeTable = new TopLevelTypeTable(BuiltinTypes.TYPE_TABLE);
        TypeInterface inf1 = new TypeInterface(typeTable, functionTable, new DexInterface("" +
                "interface Hello {\n" +
                "   Action1(): string\n" +
                "}"));
        TypeInterface inf2 = new TypeInterface(typeTable, functionTable, new DexInterface("" +
                "interface World {\n" +
                "   Action1(): string\n" +
                "   Action2(): string\n" +
                "}"));
        Assert.assertTrue(inf1.isAssignableFrom(inf2));
        Assert.assertFalse(inf2.isAssignableFrom(inf1));
    }

    @Test
    public void argument_takes_sub_type() {
        FunctionTable functionTable = new FunctionTable();
        TopLevelTypeTable typeTable = new TopLevelTypeTable(BuiltinTypes.TYPE_TABLE);
        new TypeInterface(typeTable, functionTable, new DexInterface("" +
                "interface SuperType {\n" +
                "   Action1(): string\n" +
                "}"));
        new TypeInterface(typeTable, functionTable, new DexInterface("" +
                "interface SubType {\n" +
                "   Action1(): string\n" +
                "   Action2(): string\n" +
                "}"));
        TypeInterface inf1 = new TypeInterface(typeTable, functionTable, new DexInterface("" +
                "interface Hello {\n" +
                "   Action(arg: SuperType): string\n" +
                "}"));
        TypeInterface inf2 = new TypeInterface(typeTable, functionTable, new DexInterface("" +
                "interface World {\n" +
                "   Action(arg: SubType): string\n" +
                "}"));
        Assert.assertTrue(inf1.isAssignableFrom(inf2));
        Assert.assertFalse(inf2.isAssignableFrom(inf1));
    }

    @Test
    public void ret_takes_sub_type() {
        FunctionTable functionTable = new FunctionTable();
        TopLevelTypeTable typeTable = new TopLevelTypeTable(BuiltinTypes.TYPE_TABLE);
        new TypeInterface(typeTable, functionTable, new DexInterface("" +
                "interface SuperType {\n" +
                "   Action1(): string\n" +
                "}"));
        new TypeInterface(typeTable, functionTable, new DexInterface("" +
                "interface SubType {\n" +
                "   Action1(): string\n" +
                "   Action2(): string\n" +
                "}"));
        TypeInterface inf1 = new TypeInterface(typeTable, functionTable, new DexInterface("" +
                "interface Hello {\n" +
                "   Action(): SuperType\n" +
                "}"));
        TypeInterface inf2 = new TypeInterface(typeTable, functionTable, new DexInterface("" +
                "interface World {\n" +
                "   Action(): SubType\n" +
                "}"));
        Assert.assertTrue(inf1.isAssignableFrom(inf2));
        Assert.assertFalse(inf2.isAssignableFrom(inf1));
    }

    @Test
    public void implement_interface_by_define_function() {
        FunctionTable functionTable = new FunctionTable();
        TopLevelTypeTable typeTable = new TopLevelTypeTable(BuiltinTypes.TYPE_TABLE);
        TypeInterface someInf = new TypeInterface(typeTable, functionTable, new DexInterface("" +
                "interface SomeInf {\n" +
                "   SomeAction(): string\n" +
                "}"));
        functionTable.define(new TypeFunction("SomeAction", new ArrayList<>() {{
            add(BuiltinTypes.STRING);
        }}, BuiltinTypes.STRING));
        Assert.assertFalse(BuiltinTypes.STRING.isAssignableFrom(someInf));
        Assert.assertTrue(someInf.isAssignableFrom(BuiltinTypes.STRING));
    }
}
