package com.dexscript.infer;

public class InterfaceTypeTest {

//    @Test
//    public void define_interface_type_by_member_function() {
//        ResolveType infer = new ResolveType();
//        infer.define(new DexInterface("" +
//                "interface Duck {\n" +
//                "  ::Quack(duck: Duck): string\n" +
//                "}"));
//        Denotation.InterfaceType inf = (Denotation.InterfaceType) infer.resolveType("Duck");
//        Assert.assertEquals(1, inf.members().size());
//        Assert.assertEquals("String", inf.members().get(0).ret().javaClassName());
//    }
//
//    @Test
//    public void define_interface_type_by_member_method() {
//        ResolveType infer = new ResolveType();
//        infer.define(new DexInterface("" +
//                "interface Duck {\n" +
//                "  Quack(): string\n" +
//                "}"));
//        Denotation.InterfaceType inf = (Denotation.InterfaceType) infer.resolveType("Duck");
//        Assert.assertEquals(1, inf.members().size());
//        Assert.assertEquals("String", inf.members().get(0).ret().javaClassName());
//    }
//
//    @Test
//    public void test_compatibility() {
//        ResolveType infer = new ResolveType();
//        infer.define(new DexInterface("" +
//                "interface Duck1 {\n" +
//                "  ::Quack(duck: Duck1): string\n" +
//                "}"));
//        infer.define(new DexInterface("" +
//                "interface Duck2 {\n" +
//                "  ::Quack(duck: Duck2): string\n" +
//                "}"));
//        Denotation.InterfaceType duck1 = (Denotation.InterfaceType) infer.resolveType("Duck1");
//        Denotation.InterfaceType duck2 = (Denotation.InterfaceType) infer.resolveType("Duck2");
//        Assert.assertTrue(duck1.isAssignableFrom(duck2));
//        Assert.assertTrue(duck2.isAssignableFrom(duck1));
//    }
//
//    @Test
//    public void test_subtype() {
//        ResolveType infer = new ResolveType();
//        infer.define(new DexInterface("" +
//                "interface Duck {\n" +
//                "  ::Quack(duck: Duck): string\n" +
//                "}"));
//        infer.define(new DexInterface("" +
//                "interface Donald {\n" +
//                "  ::Quack(duck: Donald): string\n" +
//                "  ::IAmDonald(duck: Duck): string\n" +
//                "}"));
//        Denotation.InterfaceType duck = (Denotation.InterfaceType) infer.resolveType("Duck");
//        Denotation.InterfaceType donald = (Denotation.InterfaceType) infer.resolveType("Donald");
//        Assert.assertTrue(duck.isAssignableFrom(donald));
//        Assert.assertFalse(donald.isAssignableFrom(duck));
//    }
//
//    @Test
//    public void implement_interface_by_function() {
//        ResolveType infer = new ResolveType();
//        infer.define(new DexInterface("interface Duck{ ::Quack(duck: Duck): string }"));
//        infer.define(new DexFunction("function Quack(i: int64): string { return 'duck'; }"));
//        Type int64 = infer.resolveType("int64");
//        Type duck = infer.resolveType("Duck");
//        Assert.assertTrue(duck.isAssignableFrom(int64));
//        Assert.assertFalse(int64.isAssignableFrom(duck));
//    }
//
//    @Test
//    public void implement_interface_by_another_interface() {
//        ResolveType infer = new ResolveType();
//        infer.define(new DexInterface("interface Quackable{ ::Quack(duck: Quackable): string }"));
//        infer.define(new DexInterface("interface Swimable{ ::Swim(duck: Swimable): string }"));
//        infer.define(new DexInterface("" +
//                "interface Duck{\n" +
//                "   ::TakeTwo(duck0: Duck, duck1: Quackable, duck2: Swimable): string\n" +
//                "}"));
//        infer.define(new DexFunction("function Quack(i: int64): string { return 'quack' }"));
//        infer.define(new DexFunction("function Swim(i: int64): string { return 'swim' }"));
//        infer.define(new DexFunction("" +
//                "function TakeTwo(duck0: int64, duck1: Swimable, duck2: Quackable): string {\n" +
//                "   return 'duck'\n" +
//                "}"));
//        Type int64 = infer.resolveType("int64");
//        Type duck = infer.resolveType("Duck");
//        Assert.assertTrue(duck.isAssignableFrom(int64));
//        Assert.assertFalse(int64.isAssignableFrom(duck));
//    }
}
