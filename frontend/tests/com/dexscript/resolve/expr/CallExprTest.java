package com.dexscript.resolve.expr;

public class CallExprTest {

//    @Test
//    public void call_concrete_function() {
//        ResolveType resolve = new ResolveType();
//        resolve.define(new DexFunction("function ToString(i: int64): string { return '' }"));
//        Assert.assertEquals(BuiltinTypes.STRING, resolve.resolveType(DexExpr.parse("ToString(100)")));
//    }
//
//    @Test
//    public void call_not_existing() {
//        ResolveType resolve = new ResolveType();
//        Assert.assertEquals(BuiltinTypes.UNDEFINED_TYPE, resolve.resolveType(DexExpr.parse("ToString(100)")));
//    }
//
//    @Test
//    public void uniform_function_call_syntax() {
//        ResolveType resolve = new ResolveType();
//        resolve.define(new DexFunction("function ToString(i: int64): string { return '' }"));
//        DexMethodCallExpr expr = (DexMethodCallExpr) DexExpr.parse("100.ToString()");
//        Assert.assertEquals(BuiltinTypes.STRING, resolve.resolveType(expr));
//    }
//
//    @Test
//    public void call_interface_function() {
//        ResolveType resolve = new ResolveType();
//        resolve.define(new DexInterface("interface Duck{ ::Quack(duck: Duck): int64 }"));
//        DexFunction function = new DexFunction("function PrintDuck(duck: Duck): int64 { return duck.Quack(); }");
//        Assert.assertEquals(BuiltinTypes.INT64_TYPE, resolve.resolveType(function.stmts().get(0).asReturn().expr()));
//    }
}
