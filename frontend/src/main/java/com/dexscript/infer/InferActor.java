package com.dexscript.infer;

import com.dexscript.ast.DexActor;
import com.dexscript.ast.elem.DexParam;
import com.dexscript.type.core.DType;
import com.dexscript.type.core.InferType;
import com.dexscript.type.core.TypeSystem;

class InferActor implements InferValue<DexActor> {

    @Override
    public void handle(TypeSystem ts, DexActor func, ValueTable table) {
        for (DexParam param : func.sig().params()) {
            String name = param.paramName().toString();
            DType type = InferType.$(ts, param.paramType());
            table.define(new Value(name, type, param));
        }
    }
}
