package com.dexscript.infer;

import com.dexscript.ast.DexFunction;
import com.dexscript.ast.expr.DexExpr;
import com.dexscript.type.BuiltinTypes;
import com.dexscript.type.Type;
import com.dexscript.type.TypeSystem;
import org.junit.Assert;
import org.junit.Test;

public class InferConsumeTest {

    @Test
    public void consume_string() {
        TypeSystem ts = new TypeSystem();
        ts.defineActor(new DexFunction("" +
                "function Hello(arg: int64): string {\n" +
                "   return 'hello'\n" +
                "}"));
        Type type = InferType.inferType(ts, DexExpr.parse("<-Hello{100}"));
        Assert.assertEquals(BuiltinTypes.STRING, type);
    }
}
