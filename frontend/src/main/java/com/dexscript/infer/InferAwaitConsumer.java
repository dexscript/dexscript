package com.dexscript.infer;

import com.dexscript.ast.stmt.DexAwaitConsumer;
import com.dexscript.type.core.*;

import java.util.Arrays;

public class InferAwaitConsumer implements InferValue<DexAwaitConsumer> {

    @Override
    public void handle(TypeSystem ts, DexAwaitConsumer awaitConsumer, ValueTable table) {
        String taskName = awaitConsumer.identifier().toString();
        DType ret = InferType.$(ts, awaitConsumer.ret());
        DType taskType = ts.typeTable().resolveType(awaitConsumer.pkg(), "Task", Arrays.asList(ret));
        table.define(new Value(taskName, taskType, awaitConsumer));
    }
}
