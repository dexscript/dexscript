package com.dexscript.infer;

import com.dexscript.ast.expr.DexExpr;
import com.dexscript.type.StringLiteralType;
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
    public void string_literal() {
        DType type = InferType.$(ts, DexExpr.parse("'hello'"));
        Assert.assertEquals(new StringLiteralType(ts, "hello"), type);
    }

    @Test
    public void integer_const() {
        DType type = InferType.$(ts, DexExpr.parse("100"));
        Assert.assertEquals(ts.constOf(100), type);
    }
}
