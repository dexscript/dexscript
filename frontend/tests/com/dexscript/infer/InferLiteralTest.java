package com.dexscript.infer;

import com.dexscript.ast.expr.DexExpr;
import com.dexscript.type.BuiltinTypes;
import org.junit.Assert;
import org.junit.Test;

public class InferLiteralTest {

    @Test
    public void string_literal() {
        Assert.assertEquals(BuiltinTypes.STRING, InferType.inferType(DexExpr.parse("'hello'")));
    }
}
