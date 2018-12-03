package com.dexscript.resolve.expr;

import com.dexscript.ast.DexFunction;
import com.dexscript.ast.DexInterface;
import com.dexscript.ast.expr.DexExpr;
import com.dexscript.ast.expr.DexMethodCallExpr;
import com.dexscript.denotation.BuiltinTypes;
import com.dexscript.resolve.Resolve;
import org.junit.Assert;
import org.junit.Test;

public class CallExprTest {

//    @Test
//    public void call_concrete_function() {
//        Resolve resolve = new Resolve();
//        resolve.define(new DexFunction("function ToString(i: int64): string { return '' }"));
//        Assert.assertEquals(BuiltinTypes.STRING, resolve.resolveType(DexExpr.parse("ToString(100)")));
//    }
//
//    @Test
//    public void call_not_existing() {
//        Resolve resolve = new Resolve();
//        Assert.assertEquals(BuiltinTypes.UNDEFINED_TYPE, resolve.resolveType(DexExpr.parse("ToString(100)")));
//    }
//
//    @Test
//    public void uniform_function_call_syntax() {
//        Resolve resolve = new Resolve();
//        resolve.define(new DexFunction("function ToString(i: int64): string { return '' }"));
//        DexMethodCallExpr expr = (DexMethodCallExpr) DexExpr.parse("100.ToString()");
//        Assert.assertEquals(BuiltinTypes.STRING, resolve.resolveType(expr));
//    }
//
//    @Test
//    public void call_interface_function() {
//        Resolve resolve = new Resolve();
//        resolve.define(new DexInterface("interface Duck{ ::Quack(duck: Duck): int64 }"));
//        DexFunction function = new DexFunction("function PrintDuck(duck: Duck): int64 { return duck.Quack(); }");
//        Assert.assertEquals(BuiltinTypes.INT64_TYPE, resolve.resolveType(function.stmts().get(0).asReturn().expr()));
//    }
}
