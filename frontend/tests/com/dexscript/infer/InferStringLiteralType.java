package com.dexscript.infer;

import com.dexscript.type.DType;
import com.dexscript.type.ResolveType;
import com.dexscript.type.StringLiteralType;
import com.dexscript.type.TypeSystem;
import org.junit.Assert;
import org.junit.Test;

public class InferStringLiteralType {

    @Test
    public void string_literal_type() {
        TypeSystem ts = new TypeSystem();
        DType type = ResolveType.$(ts, "'hello'");
        Assert.assertEquals(new StringLiteralType(ts, "hello"), type);
    }
}
