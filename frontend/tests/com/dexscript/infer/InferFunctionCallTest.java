package com.dexscript.infer;

import com.dexscript.ast.DexActor;
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

    private FunctionType func(String actorSrc) {
        DexActor actor = DexActor.$("function " + actorSrc);
        FunctionSig sig = new FunctionSig(ts, actor.sig());
        FunctionType funcType = new FunctionType(ts, actor.functionName(), sig.params(), sig.ret(), sig);
        return funcType;
    }

    @Test
    public void match_one() {
        func("Hello(arg0: string): string");
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
        func("Hello(arg0: 'a'): 'a'");
        func("Hello(arg0: string): 'b'");
        DType type = InferType.$(ts, DexExpr.parse("Hello('hello')"));
        Assert.assertTrue(IsAssignable.$(type, new StringLiteralType(ts, "a")));
        Assert.assertTrue(IsAssignable.$(type, new StringLiteralType(ts, "b")));
    }

    @Test
    public void infer_generic_function_call() {
        func("Hello(<T>: interface{}, arg0: T): T");
        DType type = InferType.$(ts, DexExpr.parse("Hello('world')"));
        Assert.assertEquals(ts.STRING, type);
    }

    @Test
    public void infer_generic_function_call_with_type_args() {
        func("Hello(<T>: interface{}, arg0: T): T");
        DType type = InferType.$(ts, DexExpr.parse("Hello<int64>(1)"));
        Assert.assertEquals(ts.INT64, type);
    }

    @Test
    public void infer_call_with_named_arg() {
        func("Hello(msg: string): string");
        DType type = InferType.$(ts, DexExpr.parse("Hello(msg='hello')"));
        Assert.assertEquals(ts.STRING, type);
    }
}
