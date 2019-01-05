package com.dexscript.infer;

import com.dexscript.ast.DexActor;
import com.dexscript.ast.elem.DexParam;
import com.dexscript.type.core.*;

class InferActor implements InferValue<DexActor> {

    @Override
    public void handle(TypeSystem ts, DexActor func, ValueTable table) {
        TypeTable localTypeTable = new TypeTable(ts, func.typeParams());
        for (DexParam param : func.sig().params()) {
            String name = param.paramName().toString();
            DType type = InferType.$(ts, localTypeTable, param.paramType());
            table.define(new Value(name, type, param));
        }
    }
}
