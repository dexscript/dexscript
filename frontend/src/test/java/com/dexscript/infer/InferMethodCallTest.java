package com.dexscript.infer;

import com.dexscript.ast.expr.DexExpr;
import com.dexscript.type.core.InferType;
import com.dexscript.type.core.DType;
import com.dexscript.type.core.FunctionParam;
import com.dexscript.type.core.FunctionType;
import com.dexscript.type.core.TypeSystem;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class InferMethodCallTest {

    @Test
    public void method_call_is_same_as_function_call() {
        TypeSystem ts = new TypeSystem();
        FunctionType func = new FunctionType(ts, "Hello", new ArrayList<FunctionParam>() {{
            add(new FunctionParam("arg0", ts.STRING));
        }}, ts.STRING);
        func.implProvider(expandedFunc -> new Object());
        ts.defineFunction(func);
        DType type = InferType.$(ts, DexExpr.$parse("'hello'.Hello()"));
        Assert.assertEquals(ts.STRING, type);
    }
}
