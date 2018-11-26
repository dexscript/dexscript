package com.dexscript.resolve;

import com.dexscript.ast.DexFunction;
import com.dexscript.ast.expr.DexExpr;
import org.junit.Assert;
import org.junit.Test;

public class ResolveTypeTest {

    @Test
    public void integer_literal_is_int64() {
        Assert.assertEquals(BuiltinTypes.INT64_TYPE, new Resolve().resolveType(DexExpr.parse("1")));
    }

    @Test
    public void float_literal_is_float64() {
        Assert.assertEquals(BuiltinTypes.FLOAT64_TYPE, new Resolve().resolveType(DexExpr.parse("1.1")));
    }

    @Test
    public void string_literal_is_string() {
        Assert.assertEquals(BuiltinTypes.STRING_TYPE, new Resolve().resolveType(DexExpr.parse("'hello'")));
    }

    @Test
    public void evaluate_function() {
        Resolve resolve = new Resolve();
        resolve.define(new DexFunction("function ToString(i: int64): string { return ''; }"));
        Assert.assertEquals(BuiltinTypes.STRING_TYPE, resolve.resolveType(DexExpr.parse("ToString(100)")));
    }

    @Test
    public void evaluate_not_existing_function() {
        Resolve resolve = new Resolve();
        Assert.assertEquals(BuiltinTypes.UNDEFINED_TYPE, resolve.resolveType(DexExpr.parse("ToString(100)")));
    }

    @Test
    public void evaluate_add() {
        Resolve resolve = new Resolve();
        resolve.define(new DexFunction("function Add__(i: int64, i: int64): int64 { return 0; }"));
        Assert.assertEquals(BuiltinTypes.INT64_TYPE, resolve.resolveType(DexExpr.parse("1+1")));
    }
}
