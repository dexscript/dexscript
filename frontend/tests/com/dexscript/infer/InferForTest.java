package com.dexscript.infer;

import com.dexscript.ast.DexActor;
import com.dexscript.ast.expr.DexValueRef;
import com.dexscript.type.TypeSystem;
import org.junit.Assert;
import org.junit.Test;

public class InferForTest {

    @Test
    public void short_var_decl() {
        DexActor func = new DexActor("" +
                "function Hello(): int64 {\n" +
                "   var total: int64\n" +
                "   for i := 0; i < 100; i++ {" +
                "       total = total + i" +
                "   }" +
                "   return total\n" +
                "}");
        DexValueRef ref = func.stmts().get(1).asFor()
                .blk().stmts().get(0).asAssign()
                .expr().asAdd().right().asRef();
        TypeSystem ts = new TypeSystem();
        Value val = InferValue.$(ts, ref);
        Assert.assertEquals(ts.INT64, val.type());
    }
}
