package com.dexscript.infer;

import com.dexscript.ast.stmt.DexAwaitConsumer;
import com.dexscript.type.DType;
import com.dexscript.type.TypeSystem;

import java.util.Arrays;

public class InferAwaitConsumer implements InferValue<DexAwaitConsumer> {

    @Override
    public void handle(TypeSystem ts, DexAwaitConsumer awaitConsumer, ValueTable table) {
        String taskName = awaitConsumer.identifier().toString();
        DType ret = ts.resolveType(awaitConsumer.ret());
        DType taskType = ts.resolveType("Task", Arrays.asList(ret));
        table.define(new Value(taskName, taskType, awaitConsumer));
    }
}
