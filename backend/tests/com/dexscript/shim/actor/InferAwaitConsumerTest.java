package com.dexscript.shim.actor;

import com.dexscript.ast.DexInterface;
import com.dexscript.ast.expr.DexValueRef;
import com.dexscript.ast.stmt.DexAwaitConsumer;
import com.dexscript.ast.stmt.DexAwaitStmt;
import com.dexscript.infer.InferType;
import com.dexscript.type.DType;
import com.dexscript.type.InterfaceType;
import com.dexscript.type.IsAssignable;
import com.dexscript.type.TypeSystem;
import org.junit.Assert;
import org.junit.Test;

public class InferAwaitConsumerTest {

    @Test
    public void reference_task() {
        TypeSystem ts = new TypeSystem();
        DexAwaitStmt awaitStmt = new DexAwaitStmt("" +
                "await {\n" +
                "   case Hello(): string {\n" +
                "       resolve 'hello' -> Hello\n" +
                "   }\n" +
                "}");
        DexAwaitConsumer awaitConsumer = awaitStmt.cases().get(0).asAwaitConsumer();
        DexValueRef target = awaitConsumer.stmts().get(0).asProduce().target();
        DType hello = InferType.$(ts, target);
        InterfaceType inf = ts.defineInterface(DexInterface.$("" +
                "interface TaskString {\n" +
                "   Resolve__(value: string)\n" +
                "}"));
        Assert.assertTrue(IsAssignable.$(inf, hello));
    }
}
