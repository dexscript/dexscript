package com.dexscript.infer;

import com.dexscript.ast.expr.DexExpr;
import com.dexscript.type.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class InferFunctionCallTest {

    @Test
    public void match_one() {
        TypeSystem ts = new TypeSystem();
        ts.defineFunction(new FunctionType("Hello", new ArrayList<DType>() {{
            add(BuiltinTypes.STRING);
        }}, BuiltinTypes.STRING));
        DType type = InferType.$(ts, DexExpr.parse("Hello('hello')"));
        Assert.assertEquals(BuiltinTypes.STRING, type);
    }

    @Test
    public void match_none() {
        TypeSystem ts = new TypeSystem();
        DType type = InferType.$(ts, DexExpr.parse("Hello('hello')"));
        Assert.assertEquals(BuiltinTypes.UNDEFINED, type);
    }

    @Test
    public void match_two() {
        TypeSystem ts = new TypeSystem();
        ts.defineFunction(new FunctionType("Hello", new ArrayList<DType>() {{
            add(BuiltinTypes.STRING);
        }}, new StringLiteralType("a")));
        ts.defineFunction(new FunctionType("Hello", new ArrayList<DType>() {{
            add(BuiltinTypes.STRING);
        }}, new StringLiteralType("b")));
        DType type = InferType.$(ts, DexExpr.parse("Hello('hello')"));
        Assert.assertTrue(type.isAssignableFrom(new StringLiteralType("a")));
        Assert.assertTrue(type.isAssignableFrom(new StringLiteralType("b")));
    }
}
