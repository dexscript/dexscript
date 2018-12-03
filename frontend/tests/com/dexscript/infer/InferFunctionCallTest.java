package com.dexscript.infer;

import com.dexscript.ast.expr.DexExpr;
import com.dexscript.type.BuiltinTypes;
import com.dexscript.type.FunctionType;
import com.dexscript.type.Type;
import com.dexscript.type.TypeSystem;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class InferFunctionCallTest {

    @Test
    public void match_one() {
        DebugUtils.turnOnDebugLog();
        TypeSystem ts = new TypeSystem();
        ts.defineFunction(new FunctionType("Hello", new ArrayList<>() {{
            add(BuiltinTypes.STRING);
        }}, BuiltinTypes.STRING));
        Type type = InferType.inferType(ts, DexExpr.parse("Hello('hello')"));
        Assert.assertEquals(BuiltinTypes.STRING, type);
    }
}
