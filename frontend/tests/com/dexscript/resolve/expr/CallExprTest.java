package com.dexscript.resolve.expr;

import com.dexscript.ast.DexFunction;
import com.dexscript.ast.DexInterface;
import com.dexscript.ast.expr.DexExpr;
import com.dexscript.ast.expr.DexFunctionCallExpr;
import com.dexscript.ast.expr.DexMethodCallExpr;
import com.dexscript.resolve.BuiltinTypes;
import com.dexscript.resolve.Denotation;
import com.dexscript.resolve.Resolve;
import org.junit.Assert;
import org.junit.Test;

public class CallExprTest {

    @Test
    public void function_call() {
        Resolve resolve = new Resolve();
        resolve.define(new DexFunction("function ToString(i: int64): string { return ''; }"));
        Assert.assertEquals(BuiltinTypes.STRING_TYPE, resolve.resolveType(DexExpr.parse("ToString(100)")));
    }

    @Test
    public void call_not_existing() {
        Resolve resolve = new Resolve();
        Assert.assertEquals(BuiltinTypes.UNDEFINED_TYPE, resolve.resolveType(DexExpr.parse("ToString(100)")));
    }

    @Test
    public void method_call() {
        Resolve resolve = new Resolve();
        resolve.define(new DexFunction("function ToString(i: int64): string { return ''; }"));
        DexMethodCallExpr expr = (DexMethodCallExpr) DexExpr.parse("100.ToString()");
        Assert.assertEquals(BuiltinTypes.STRING_TYPE, resolve.resolveType(expr));
    }

    @Test
    public void function_with_interface_argument() {
        Resolve resolve = new Resolve();
        resolve.define(new DexInterface("interface Duck{ ::Quack(duck: Duck): string }"));
        resolve.define(new DexFunction("function Quack(i: int64): string { return 'duck'; }"));
        resolve.define(new DexFunction("function PrintDuck(duck: Duck): string { return duck.Quack(); }"));
        DexFunctionCallExpr expr = (DexFunctionCallExpr) DexExpr.parse("PrintDuck(100)");
        Assert.assertEquals(BuiltinTypes.STRING_TYPE, resolve.resolveType(expr));
    }

    @Test
    public void call_method_of_interface() {
        Resolve resolve = new Resolve();
        resolve.define(new DexInterface("" +
                "interface Duck {\n" +
                "   ::Quack(duck: Duck): string\n" +
                "}"));
        DexFunction function = new DexFunction("" +
                "function Hello(duck: Duck): string {" +
                "   return duck.Quack()\n" +
                "}");
        Denotation type = resolve.resolveType(function.stmts().get(0).asReturn().expr().asMethodCall());
        Assert.assertEquals(BuiltinTypes.STRING_TYPE, type);
    }
}
