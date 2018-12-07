package com.dexscript.type;

import com.dexscript.ast.DexInterface;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class InterfaceTypeTest {

    @Test
    public void assignable_to_same_structure() {
        FunctionTable functionTable = new FunctionTable();
        TopLevelTypeTable typeTable = new TopLevelTypeTable(BuiltinTypes.TYPE_TABLE);
        InterfaceType inf1 = new InterfaceType(typeTable, functionTable, new DexInterface("" +
                "interface Hello {\n" +
                "   Action1(): string\n" +
                "   Action2(): string\n" +
                "}"));
        InterfaceType inf2 = new InterfaceType(typeTable, functionTable, new DexInterface("" +
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
        InterfaceType inf1 = new InterfaceType(typeTable, functionTable, new DexInterface("" +
                "interface Hello {\n" +
                "   Action1(): string\n" +
                "}"));
        InterfaceType inf2 = new InterfaceType(typeTable, functionTable, new DexInterface("" +
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
        new InterfaceType(typeTable, functionTable, new DexInterface("" +
                "interface SuperType {\n" +
                "   Action1(): string\n" +
                "}"));
        new InterfaceType(typeTable, functionTable, new DexInterface("" +
                "interface SubType {\n" +
                "   Action1(): string\n" +
                "   Action2(): string\n" +
                "}"));
        InterfaceType inf1 = new InterfaceType(typeTable, functionTable, new DexInterface("" +
                "interface Hello {\n" +
                "   Action(arg: SuperType): string\n" +
                "}"));
        InterfaceType inf2 = new InterfaceType(typeTable, functionTable, new DexInterface("" +
                "interface World {\n" +
                "   Action(arg: SubType): string\n" +
                "}"));
        Assert.assertFalse(inf1.isAssignableFrom(inf2));
        Assert.assertTrue(inf2.isAssignableFrom(inf1));
    }

    @Test
    public void ret_takes_sub_type() {
        FunctionTable functionTable = new FunctionTable();
        TopLevelTypeTable typeTable = new TopLevelTypeTable(BuiltinTypes.TYPE_TABLE);
        new InterfaceType(typeTable, functionTable, new DexInterface("" +
                "interface SuperType {\n" +
                "   Action1(): string\n" +
                "}"));
        new InterfaceType(typeTable, functionTable, new DexInterface("" +
                "interface SubType {\n" +
                "   Action1(): string\n" +
                "   Action2(): string\n" +
                "}"));
        InterfaceType inf1 = new InterfaceType(typeTable, functionTable, new DexInterface("" +
                "interface Hello {\n" +
                "   Action(): SuperType\n" +
                "}"));
        InterfaceType inf2 = new InterfaceType(typeTable, functionTable, new DexInterface("" +
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
        InterfaceType someInf = new InterfaceType(typeTable, functionTable, new DexInterface("" +
                "interface SomeInf {\n" +
                "   SomeAction(): string\n" +
                "}"));
        functionTable.define(new FunctionType("SomeAction", new ArrayList<Type>() {{
            add(BuiltinTypes.STRING);
        }}, BuiltinTypes.STRING));
        Assert.assertFalse(BuiltinTypes.STRING.isAssignableFrom(someInf));
        Assert.assertTrue(someInf.isAssignableFrom(BuiltinTypes.STRING));
    }

    @Test
    public void argument_is_sub_type_can_still_implement() {
        FunctionTable functionTable = new FunctionTable();
        TopLevelTypeTable typeTable = new TopLevelTypeTable(BuiltinTypes.TYPE_TABLE);
        InterfaceType quackable = new InterfaceType(typeTable, functionTable, new DexInterface(
                "interface Quackable{ Quack(): string }"));
        InterfaceType swimable = new InterfaceType(typeTable, functionTable, new DexInterface(
                "interface Swimable{ Swim(): string }"));
        InterfaceType duck = new InterfaceType(typeTable, functionTable, new DexInterface(
                "interface Duck{\n" +
                        "   DoBoth(duck1: int64, duck2: Swimable): string\n" +
                        "}"));
        functionTable.define(new FunctionType("Quack", new ArrayList<Type>() {{
            add(BuiltinTypes.INT64);
        }}, BuiltinTypes.STRING));
        functionTable.define(new FunctionType("Swim", new ArrayList<Type>() {{
            add(BuiltinTypes.INT64);
        }}, BuiltinTypes.STRING));
        functionTable.define(new FunctionType("DoBoth", new ArrayList<Type>() {{
            add(BuiltinTypes.INT64);
            add(quackable);
            add(swimable);
        }}, BuiltinTypes.STRING));
        Assert.assertTrue(swimable.isAssignableFrom(BuiltinTypes.INT64));
        Assert.assertTrue(quackable.isAssignableFrom(BuiltinTypes.INT64));
        Assert.assertTrue(duck.isAssignableFrom(BuiltinTypes.INT64));
    }
}
