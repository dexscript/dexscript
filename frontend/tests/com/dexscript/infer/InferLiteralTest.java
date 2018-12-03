package com.dexscript.infer;

import com.dexscript.ast.expr.DexExpr;
import com.dexscript.type.BuiltinTypes;
import com.dexscript.type.TypeSystem;
import org.junit.Assert;
import org.junit.Test;

public class InferLiteralTest {

    @Test
    public void string_literal() {
        Assert.assertEquals(BuiltinTypes.STRING, InferType.inferType(new TypeSystem(), DexExpr.parse("'hello'")));
    }
}
