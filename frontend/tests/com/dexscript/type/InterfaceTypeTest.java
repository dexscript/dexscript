package com.dexscript.type;

import com.dexscript.ast.DexInterface;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class InterfaceTypeTest {

    private TypeSystem ts;

    @Before
    public void setup() {
        ts = new TypeSystem();
    }

    @Test
    public void assignable_to_same_structure() {
        InterfaceType inf1 = new InterfaceType(ts, new DexInterface("" +
                "interface Hello {\n" +
                "   Action1(): string\n" +
                "   Action2(): string\n" +
                "}"));
        InterfaceType inf2 = new InterfaceType(ts, new DexInterface("" +
                "interface World {\n" +
                "   Action1(): string\n" +
                "   Action2(): string\n" +
                "}"));
        Assert.assertTrue(inf1.isAssignableFrom(inf2));
        Assert.assertTrue(inf2.isAssignableFrom(inf1));
    }

    @Test
    public void sub_type_has_more_member() {
        InterfaceType inf1 = new InterfaceType(ts, new DexInterface("" +
                "interface Hello {\n" +
                "   Action1(): string\n" +
                "}"));
        InterfaceType inf2 = new InterfaceType(ts, new DexInterface("" +
                "interface World {\n" +
                "   Action1(): string\n" +
                "   Action2(): string\n" +
                "}"));
        Assert.assertTrue(inf1.isAssignableFrom(inf2));
        Assert.assertFalse(inf2.isAssignableFrom(inf1));
    }

    @Test
    public void argument_takes_sub_type() {
        new InterfaceType(ts, new DexInterface("" +
                "interface SuperType {\n" +
                "   Action1(): string\n" +
                "}"));
        new InterfaceType(ts, new DexInterface("" +
                "interface SubType {\n" +
                "   Action1(): string\n" +
                "   Action2(): string\n" +
                "}"));
        InterfaceType inf1 = new InterfaceType(ts, new DexInterface("" +
                "interface Hello {\n" +
                "   Action(arg: SuperType): string\n" +
                "}"));
        InterfaceType inf2 = new InterfaceType(ts, new DexInterface("" +
                "interface World {\n" +
                "   Action(arg: SubType): string\n" +
                "}"));
        Assert.assertFalse(inf1.isAssignableFrom(inf2));
        Assert.assertTrue(inf2.isAssignableFrom(inf1));
    }

    @Test
    public void ret_takes_sub_type() {
        new InterfaceType(ts, new DexInterface("" +
                "interface SuperType {\n" +
                "   Action1(): string\n" +
                "}"));
        new InterfaceType(ts, new DexInterface("" +
                "interface SubType {\n" +
                "   Action1(): string\n" +
                "   Action2(): string\n" +
                "}"));
        InterfaceType inf1 = new InterfaceType(ts, new DexInterface("" +
                "interface Hello {\n" +
                "   Action(): SuperType\n" +
                "}"));
        InterfaceType inf2 = new InterfaceType(ts, new DexInterface("" +
                "interface World {\n" +
                "   Action(): SubType\n" +
                "}"));
        Assert.assertTrue(inf1.isAssignableFrom(inf2));
        Assert.assertFalse(inf2.isAssignableFrom(inf1));
    }

    @Test
    public void implement_interface_by_define_function() {
        InterfaceType someInf = new InterfaceType(ts, new DexInterface("" +
                "interface SomeInf {\n" +
                "   SomeAction(): string\n" +
                "}"));
        ts.defineFunction(new FunctionType(ts, "SomeAction", new ArrayList<DType>() {{
            add(ts.STRING);
        }}, ts.STRING));
        Assert.assertFalse(ts.STRING.isAssignableFrom(someInf));
        Assert.assertTrue(someInf.isAssignableFrom(ts.STRING));
    }

    @Test
    public void argument_is_sub_type_can_still_implement() {
        InterfaceType quackable = new InterfaceType(ts, new DexInterface(
                "interface Quackable{ Quack(): string }"));
        InterfaceType swimable = new InterfaceType(ts, new DexInterface(
                "interface Swimable{ Swim(): string }"));
        InterfaceType duck = new InterfaceType(ts, new DexInterface(
                "interface Duck{\n" +
                        "   DoBoth(duck1: int64, duck2: Swimable): string\n" +
                        "}"));
        ts.defineFunction(new FunctionType(ts, "Quack", new ArrayList<DType>() {{
            add(ts.INT64);
        }}, ts.STRING));
        ts.defineFunction(new FunctionType(ts, "Swim", new ArrayList<DType>() {{
            add(ts.INT64);
        }}, ts.STRING));
        ts.defineFunction(new FunctionType(ts, "DoBoth", new ArrayList<DType>() {{
            add(ts.INT64);
            add(quackable);
            add(swimable);
        }}, ts.STRING));
        Assert.assertTrue(swimable.isAssignableFrom(ts.INT64));
        Assert.assertTrue(quackable.isAssignableFrom(ts.INT64));
        Assert.assertTrue(duck.isAssignableFrom(ts.INT64));
    }
}
