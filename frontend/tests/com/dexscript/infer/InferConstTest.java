package com.dexscript.infer;

import com.dexscript.ast.expr.DexExpr;
import com.dexscript.type.DType;
import com.dexscript.type.TypeSystem;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class InferConstTest {

    private TypeSystem ts;

    @Before
    public void setup() {
        ts = new TypeSystem();
    }

    @Test
    public void string_const() {
        DType type = InferType.$(ts, DexExpr.$parse("'hello'"));
        Assert.assertEquals(ts.constOf("hello"), type);
    }

    @Test
    public void integer_const() {
        DType type = InferType.$(ts, DexExpr.$parse("100"));
        Assert.assertEquals(ts.constOf(100), type);
    }

    @Test
    public void float_const() {
        DType type = InferType.$(ts, DexExpr.$parse("100.0"));
        Assert.assertEquals(ts.constOf(100.0), type);
    }

    @Test
    public void bool_const() {
        DType type = InferType.$(ts, DexExpr.$parse("true"));
        Assert.assertEquals(ts.constOf(true), type);
    }
}
