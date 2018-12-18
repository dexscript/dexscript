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

    private void func(String src) {
        DexActor actor = new DexActor("function " + src);
        FunctionSig sig = new FunctionSig(ts, actor.sig());
        FunctionType function = new FunctionType(ts, actor.functionName(), sig.params(), sig.ret(), sig);
        function.setImpl(new Object());
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
        IsAssignable isAssignable = new IsAssignable(inf1, inf2);
        Assert.assertTrue(isAssignable.result());
        isAssignable = new IsAssignable(inf2, inf1);
        Assert.assertTrue(isAssignable.result());
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
        IsAssignable isAssignable = new IsAssignable(inf1, inf2);
        Assert.assertTrue(isAssignable.result());
        isAssignable = new IsAssignable(inf2, inf1);
        Assert.assertFalse(isAssignable.result());
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
        IsAssignable isAssignable = new IsAssignable(inf1, inf2);
        Assert.assertFalse(isAssignable.result());
        isAssignable = new IsAssignable(inf2, inf1);
        Assert.assertTrue(isAssignable.result());
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
        IsAssignable isAssignable = new IsAssignable(inf1, inf2);
        Assert.assertTrue(isAssignable.result());
        isAssignable = new IsAssignable(inf2, inf1);
        Assert.assertFalse(isAssignable.result());
    }

    @Test
    public void implement_interface_by_define_function() {
        InterfaceType someInf = new InterfaceType(ts, new DexInterface("" +
                "interface SomeInf {\n" +
                "   SomeAction(): string\n" +
                "}"));
        func("SomeAction(arg0: string): string");
        IsAssignable isAssignable = new IsAssignable(ts.STRING, someInf);
        Assert.assertFalse(isAssignable.result());
        isAssignable = new IsAssignable(someInf, ts.STRING);
        Assert.assertTrue(isAssignable.result());
    }

    @Test
    public void argument_is_sub_type_can_still_implement() {
        InterfaceType quackable = new InterfaceType(ts, new DexInterface(
                "interface Quackable{ Quack(): string }"));
        InterfaceType swimable = new InterfaceType(ts, new DexInterface(
                "interface Swimable{ Swim(): string }"));
        InterfaceType duck = new InterfaceType(ts, new DexInterface(
                "interface Duck{\n" +
                        "   DoBoth(duck1: Quackable, duck2: Swimable): string\n" +
                        "}"));
        func("Quack(arg0: int64): string");
        func("Swim(arg0: int64): string");
        func("DoBoth(arg0: int64, arg1: Quackable, arg2: Swimable): string");
        IsAssignable isAssignable = new IsAssignable(swimable, ts.INT64);
        Assert.assertTrue(isAssignable.result());
        isAssignable = new IsAssignable(quackable, ts.INT64);
        Assert.assertTrue(isAssignable.result());
        isAssignable = new IsAssignable(duck, ts.INT64);
        Assert.assertTrue(isAssignable.result());
    }
}
