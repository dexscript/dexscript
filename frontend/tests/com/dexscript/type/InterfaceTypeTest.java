package com.dexscript.type;

import com.dexscript.ast.DexActor;
import com.dexscript.ast.DexInterface;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class InterfaceTypeTest {

    private TypeSystem ts;

    @Before
    public void setup() {
        ts = new TypeSystem();
    }

    private void defineFunction(String src) {
        DexActor actor = new DexActor("function " + src);
        FunctionSig sig = new FunctionSig(ts, actor.sig());
        FunctionType function = new FunctionType(ts, actor.functionName(), sig.params(), sig.ret(), sig);
        function.setImpl(new Object());
        ts.defineFunction(function);
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
        Assert.assertTrue(new TypeComparison(inf1, inf2).isAssignable());
        Assert.assertFalse(new TypeComparison(inf2, inf1).isAssignable());
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
        defineFunction("SomeAction(arg0: string): string");
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
        defineFunction("Quack(arg0: int64): string");
        defineFunction("Swim(arg0: int64): string");
        defineFunction("DoBoth(arg0: int64, arg1: Quackable, arg2: Swimable): string");
        Assert.assertTrue(new TypeComparison(swimable, ts.INT64).isAssignable());
        Assert.assertTrue(new TypeComparison(quackable, ts.INT64).isAssignable());
        Assert.assertTrue(new TypeComparison(duck, ts.INT64).isAssignable());
    }
}
