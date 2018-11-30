package com.dexscript.resolve.expr;

import com.dexscript.ast.expr.DexExpr;
import com.dexscript.resolve.BuiltinTypes;
import com.dexscript.resolve.Resolve;
import org.junit.Assert;
import org.junit.Test;

public class LiteralTest {

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
}
