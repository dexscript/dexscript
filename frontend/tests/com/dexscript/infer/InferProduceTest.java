package com.dexscript.infer;

import com.dexscript.ast.DexFunction;
import com.dexscript.ast.expr.DexExpr;
import com.dexscript.ast.func.DexAwaitConsumer;
import com.dexscript.ast.func.DexAwaitStmt;
import com.dexscript.type.BuiltinTypes;
import com.dexscript.type.Type;
import com.dexscript.type.TypeSystem;
import org.junit.Assert;
import org.junit.Test;

public class InferProduceTest {

    @Test
    public void produce_string() {
        TypeSystem ts = new TypeSystem();
        DexAwaitStmt awaitStmt = new DexAwaitStmt("" +
                "await {\n" +
                "   case Hello(): string {\n" +
                "       produce 'hello' -> Hello\n" +
                "   }\n" +
                "}");
        DexAwaitConsumer awaitConsumer = awaitStmt.cases().get(0).asAwaitConsumer();
//        Type type = InferType.$(ts, awaitConsumer.stmts().get(0).asExpr());
//        Assert.assertEquals(BuiltinTypes.STRING, type);
    }
}
