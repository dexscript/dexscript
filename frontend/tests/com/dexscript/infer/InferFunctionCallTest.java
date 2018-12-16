package com.dexscript.infer;

import com.dexscript.ast.expr.DexExpr;
import com.dexscript.type.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class InferFunctionCallTest {

    private TypeSystem ts;

    @Before
    public void setup() {
        ts = new TypeSystem();
    }

    @Test
    public void match_one() {
        ts.defineFunction(new FunctionType(ts, "Hello", new ArrayList<DType>() {{
            add(ts.STRING);
        }}, ts.STRING));
        DType type = InferType.$(ts, DexExpr.parse("Hello('hello')"));
        Assert.assertEquals(ts.STRING, type);
    }

    @Test
    public void match_none() {
        DType type = InferType.$(ts, DexExpr.parse("Hello('hello')"));
        Assert.assertEquals(ts.UNDEFINED, type);
    }

    @Test
    public void match_two() {
        ts.defineFunction(new FunctionType(ts, "Hello", new ArrayList<DType>() {{
            add(ts.STRING);
        }}, new StringLiteralType(ts, "a")));
        ts.defineFunction(new FunctionType(ts, "Hello", new ArrayList<DType>() {{
            add(ts.STRING);
        }}, new StringLiteralType(ts, "b")));
        DType type = InferType.$(ts, DexExpr.parse("Hello('hello')"));
        Assert.assertTrue(type.isAssignableFrom(new StringLiteralType(ts, "a")));
        Assert.assertTrue(type.isAssignableFrom(new StringLiteralType(ts, "b")));
    }
}
