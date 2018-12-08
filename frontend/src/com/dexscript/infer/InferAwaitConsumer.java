package com.dexscript.infer;

import com.dexscript.ast.func.DexAwaitConsumer;
import com.dexscript.type.Type;
import com.dexscript.type.TypeSystem;

public class InferAwaitConsumer implements InferValue<DexAwaitConsumer> {
    @Override
    public void handle(TypeSystem ts, DexAwaitConsumer awaitConsumer, ValueTable table) {
        String taskName = awaitConsumer.identifier().toString();
        Type taskType = InferType.$(ts, awaitConsumer);
        table.define(new Value(taskName, taskType, awaitConsumer));
    }
}
