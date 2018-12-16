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
        DType type = InferType.$(new TypeSystem(), DexExpr.parse("'hello'"));
        Assert.assertEquals(new StringLiteralType("hello"), type);
    }
}
