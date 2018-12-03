package com.dexscript.infer;

import com.dexscript.ast.DexInterface;
import com.dexscript.ast.expr.DexExpr;
import com.dexscript.type.InterfaceType;
import com.dexscript.type.Type;
import com.dexscript.type.TypeSystem;
import org.junit.Assert;
import org.junit.Test;

public class InferNewTest {

    @Test
    public void match_one() {
        DebugUtils.turnOnDebugLog();
        TypeSystem ts = new TypeSystem();
        Type type = InferType.inferType(ts, DexExpr.parse("Hello{100}"));
        InterfaceType promise = ts.defineInterface(new DexInterface("" +
                "interface PromiseString {\n" +
                "   Consume__(): string\n" +
                "}"));
        Assert.assertTrue(promise.isAssignableFrom(type));
    }
}
