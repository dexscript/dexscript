package com.dexscript.infer;

import com.dexscript.ast.expr.DexExpr;
import com.dexscript.type.StringLiteralType;
import com.dexscript.type.DType;
import com.dexscript.type.TypeSystem;
import org.junit.Assert;
import org.junit.Test;

public class InferLiteralTest {

    @Test
    public void string_literal() {
        TypeSystem ts = new TypeSystem();
        DType type = InferType.$(ts, DexExpr.parse("'hello'"));
        Assert.assertEquals(new StringLiteralType(ts, "hello"), type);
    }
}
