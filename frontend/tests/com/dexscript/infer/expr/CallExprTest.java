package com.dexscript.infer.expr;

public class CallExprTest {

//    @Test
//    public void call_concrete_function() {
//        ResolveType infer = new ResolveType();
//        infer.define(new DexFunction("function ToString(i: int64): string { return '' }"));
//        Assert.assertEquals(BuiltinTypes.STRING, infer.resolveType(DexExpr.parse("ToString(100)")));
//    }
//
//    @Test
//    public void call_not_existing() {
//        ResolveType infer = new ResolveType();
//        Assert.assertEquals(BuiltinTypes.UNDEFINED_TYPE, infer.resolveType(DexExpr.parse("ToString(100)")));
//    }
//
//    @Test
//    public void uniform_function_call_syntax() {
//        ResolveType infer = new ResolveType();
//        infer.define(new DexFunction("function ToString(i: int64): string { return '' }"));
//        DexMethodCallExpr expr = (DexMethodCallExpr) DexExpr.parse("100.ToString()");
//        Assert.assertEquals(BuiltinTypes.STRING, infer.resolveType(expr));
//    }
//
//    @Test
//    public void call_interface_function() {
//        ResolveType infer = new ResolveType();
//        infer.define(new DexInterface("interface Duck{ ::Quack(duck: Duck): int64 }"));
//        DexFunction function = new DexFunction("function PrintDuck(duck: Duck): int64 { return duck.Quack(); }");
//        Assert.assertEquals(BuiltinTypes.INT64_TYPE, infer.resolveType(function.stmts().get(0).asReturn().expr()));
//    }
}
