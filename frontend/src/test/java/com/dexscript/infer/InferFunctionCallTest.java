package com.dexscript.infer;

import com.dexscript.ast.DexActor;
import com.dexscript.ast.expr.DexExpr;
import com.dexscript.type.core.DType;
import com.dexscript.type.core.FunctionType;
import com.dexscript.type.core.TypeSystem;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class InferFunctionCallTest {

    private TypeSystem ts;

    @Before
    public void setup() {
        ts = new TypeSystem();
    }

    private FunctionType func(String actorSrc) {
        DexActor actor = DexActor.$("function " + actorSrc);
        FunctionType funcType = new FunctionType(ts, actor.functionName(), null, actor.sig());
        funcType.implProvider(expandedFunc -> new Object());
        return funcType;
    }

    @Test
    public void match_one() {
        func("Hello(arg0: string): string");
        DType type = InferType.$(ts, DexExpr.$parse("Hello('hello')"));
        Assert.assertEquals(ts.STRING, type);
    }

    @Test
    public void match_none() {
        DType type = InferType.$(ts, DexExpr.$parse("Hello('hello')"));
        Assert.assertEquals(ts.UNDEFINED, type);
    }

    @Test
    public void infer_generic_function_call() {
        func("Hello(<T>: interface{}, arg0: T): T");
        DType type = InferType.$(ts, DexExpr.$parse("Hello('world')"));
        Assert.assertEquals(ts.literalOf("world"), type);
    }

    @Test
    public void infer_generic_function_call_with_type_args() {
        func("Hello(<T>: interface{}, arg0: T): T");
        DType type = InferType.$(ts, DexExpr.$parse("Hello<int64>(1)"));
        Assert.assertEquals(ts.INT64, type);
    }

    @Test
    public void infer_call_with_named_arg() {
        func("Hello(msg: string): string");
        DType type = InferType.$(ts, DexExpr.$parse("Hello(msg='hello')"));
        Assert.assertEquals(ts.STRING, type);
    }

    @Test
    public void infer_call_with_context() {
        func("World()");
        func("Hello(): string");
        DType type = InferType.$(ts, DexExpr.$parse("Hello($=new World())"));
        Assert.assertEquals(ts.STRING, type);
    }
}
