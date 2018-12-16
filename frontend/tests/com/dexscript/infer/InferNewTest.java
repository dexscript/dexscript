package com.dexscript.infer;

import com.dexscript.ast.expr.DexExpr;
import com.dexscript.type.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class InferNewTest {

    @Test
    public void match_one() {
        TypeSystem ts = new TypeSystem();
        List<DType> params = ResolveType.$(ts.typeTable(), "'Hello'", "int64");
        ts.defineFunction(new FunctionType("New__", params, BuiltinTypes.STRING));
        DType type = InferType.$(ts, DexExpr.parse("new Hello(100)"));
        Assert.assertEquals(BuiltinTypes.STRING, type);
    }
}
