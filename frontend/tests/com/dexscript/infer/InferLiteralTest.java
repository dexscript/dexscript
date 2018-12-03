package com.dexscript.infer;

import com.dexscript.ast.expr.DexExpr;
import com.dexscript.type.StringLiteralType;
import com.dexscript.type.Type;
import com.dexscript.type.TypeSystem;
import org.junit.Assert;
import org.junit.Test;

public class InferLiteralTest {

    @Test
    public void string_literal() {
        Type type = InferType.inferType(new TypeSystem(), DexExpr.parse("'hello'"));
        Assert.assertEquals(new StringLiteralType("hello"), type);
    }
}
