package com.dexscript.infer;

import com.dexscript.ast.DexActor;
import com.dexscript.ast.elem.DexParam;
import com.dexscript.type.Type;
import com.dexscript.type.TypeSystem;

class InferFunction implements InferValue<DexActor> {

    @Override
    public void handle(TypeSystem ts, DexActor func, ValueTable table) {
        for (DexParam param : func.sig().params()) {
            String name = param.paramName().toString();
            Type type = ts.resolveType(param.paramType());
            table.define(new Value(name, type, param));
        }
    }
}
