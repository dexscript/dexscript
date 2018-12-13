package com.dexscript.transpile.type;

import com.dexscript.ast.DexActor;
import com.dexscript.ast.expr.DexExpr;
import com.dexscript.infer.InferType;
import com.dexscript.transpile.shim.OutShim;
import com.dexscript.transpile.type.actor.ActorType;
import com.dexscript.type.BuiltinTypes;
import com.dexscript.type.Type;
import com.dexscript.type.TypeSystem;
import org.junit.Assert;
import org.junit.Test;

public class InferConsumeTest {

    @Test
    public void consume_string() {
        TypeSystem ts = new TypeSystem();
        OutShim oShim = new OutShim(ts);
        new ActorType(oShim, new DexActor("" +
                "function Hello(arg: int64): string {\n" +
                "   return 'hello'\n" +
                "}"), null);
        Type type = InferType.$(ts, DexExpr.parse("<-Hello{100}"));
        Assert.assertEquals(BuiltinTypes.STRING, type);
    }
}
