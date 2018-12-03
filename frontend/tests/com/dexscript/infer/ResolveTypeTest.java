package com.dexscript.infer;

public class ResolveTypeTest {

//    @Test
//    public void reference_builtin_type() {
//        DexFunction function = new DexFunction("function hello(): string {}");
//        Denotation retType = new ResolveType().resolveType(function.sig().ret());
//        Assert.assertEquals(BuiltinTypes.STRING, retType);
//    }
//
//    @Test
//    public void reference_interface() {
//        DexInterface duckInf = new DexInterface("interface Duck{}");
//        ResolveType infer = new ResolveType();
//        infer.define(duckInf);
//        DexFunction function = new DexFunction("function hello(): Duck {}");
//        Denotation retType = new ResolveType().resolveType(function.sig().ret());
//        Assert.assertEquals("Duck", retType.name());
//    }
//
//    @Test
//    public void reference_function_as_interface() {
//        ResolveType infer = new ResolveType();
//        infer.define(new DexFunction("" +
//                "function Hello(): string {\n" +
//                "   return 'hello'\n" +
//                "}"));
//        infer.define(new DexInterface("" +
//                "interface StringPromise {\n" +
//                "   GetResult__(): string" +
//                "}"));
//        Type inf = infer.resolveType("Hello");
//        Assert.assertEquals("Hello", inf.toString());
//        Type stringPromise = infer.resolveType("StringPromise");
//        Assert.assertTrue(stringPromise.isAssignableFrom(inf));
//    }
}
