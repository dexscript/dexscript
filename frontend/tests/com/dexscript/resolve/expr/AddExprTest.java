package com.dexscript.resolve.expr;

import com.dexscript.ast.DexFunction;
import com.dexscript.ast.expr.DexExpr;
import com.dexscript.denotation.BuiltinTypes;
import com.dexscript.resolve.Resolve;
import org.junit.Assert;
import org.junit.Test;

public class AddExprTest {

//    @Test
//    public void evaluate_add() {
//        Resolve resolve = new Resolve();
//        resolve.define(new DexFunction("function Add__(i: int64, i: int64): int64 { return 0; }"));
//        Assert.assertEquals(BuiltinTypes.INT64_TYPE, resolve.resolveType(DexExpr.parse("1+1")));
//    }
//
//    @Test
//    public void evaluate_multi_dispatched_add() {
//        Resolve resolve = new Resolve();
//        resolve.define(new DexFunction("function Add__(i: float64, i: float64): float64 { return 0.0; }"));
//        resolve.define(new DexFunction("function Add__(i: int64, i: int64): int64 { return 0; }"));
//        Assert.assertEquals(BuiltinTypes.INT64_TYPE, resolve.resolveType(DexExpr.parse("1+1")));
//    }
}
