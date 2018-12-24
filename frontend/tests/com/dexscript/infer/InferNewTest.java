package com.dexscript.infer;

import com.dexscript.ast.DexActor;
import com.dexscript.ast.expr.DexExpr;
import com.dexscript.type.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class InferNewTest {

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
        func("New__(arg0: 'Hello', arg1: int64): string");
        DType type = InferType.$(ts, DexExpr.$parse("new Hello(100)"));
        Assert.assertEquals(ts.STRING, type);
    }

    @Test
    public void invoke_non_generic_with_type_args() {
        func("New__(arg0: 'Hello', arg1: int64): string");
        DType type = InferType.$(ts, DexExpr.$parse("new Hello<int64>(100)"));
        Assert.assertEquals(ts.UNDEFINED, type);
    }
}
