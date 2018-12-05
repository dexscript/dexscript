package com.dexscript.infer;

import com.dexscript.ast.type.DexStringLiteralType;
import com.dexscript.type.StringLiteralType;
import com.dexscript.type.Type;
import com.dexscript.type.TypeSystem;
import org.junit.Assert;
import org.junit.Test;

public class InferStringLiteralType {

    @Test
    public void string_literal_type() {
        Type type = InferType.inferType(new TypeSystem(), new DexStringLiteralType("'hello'"));
        Assert.assertEquals(new StringLiteralType("hello"), type);
    }
}
