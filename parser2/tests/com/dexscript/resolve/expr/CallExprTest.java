package com.dexscript.resolve.expr;

import com.dexscript.ast.DexFunction;
import com.dexscript.ast.expr.DexExpr;
import com.dexscript.resolve.BuiltinTypes;
import com.dexscript.resolve.Resolve;
import org.junit.Assert;
import org.junit.Test;

public class CallExprTest {

    @Test
    public void function_call() {
        Resolve resolve = new Resolve();
        resolve.declare(new DexFunction("function ToString(i: int64): string { return ''; }"));
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
        resolve.declare(new DexFunction("function ToString(i: int64): string { return ''; }"));
        Assert.assertEquals(BuiltinTypes.STRING_TYPE, resolve.resolveType(DexExpr.parse("100.ToString()")));
    }
}
