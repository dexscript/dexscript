package com.dexscript.infer;

import com.dexscript.ast.func.DexAwaitConsumer;
import com.dexscript.type.Type;
import com.dexscript.type.TypeSystem;

import java.util.Arrays;

public class InferAwaitConsumer implements InferValue<DexAwaitConsumer> {

    @Override
    public void handle(TypeSystem ts, DexAwaitConsumer awaitConsumer, ValueTable table) {
        String taskName = awaitConsumer.identifier().toString();
        Type ret = ts.resolveType(awaitConsumer.ret());
        Type taskType = ts.resolveType("Task", Arrays.asList(ret));
        table.define(new Value(taskName, taskType, awaitConsumer));
    }
}
