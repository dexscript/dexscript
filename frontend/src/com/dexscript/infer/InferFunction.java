package com.dexscript.infer;

import com.dexscript.ast.DexFunction;
import com.dexscript.ast.elem.DexParam;
import com.dexscript.type.Type;
import com.dexscript.type.TypeSystem;

class InferFunction implements InferValue<DexFunction> {

    @Override
    public void handle(TypeSystem ts, DexFunction func, ValueTable table) {
        for (DexParam param : func.sig().params()) {
            String name = param.paramName().toString();
            Type type = ts.resolveType(param.paramType());
            table.define(new Value(name, type, param));
        }
    }
}
