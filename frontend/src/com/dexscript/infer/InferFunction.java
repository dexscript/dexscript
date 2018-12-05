package com.dexscript.infer;

import com.dexscript.ast.DexFunction;
import com.dexscript.ast.DexParam;
import com.dexscript.ast.core.DexElement;
import com.dexscript.type.Type;
import com.dexscript.type.TypeSystem;

class InferFunction implements InferValue {

    @Override
    public void handle(TypeSystem ts, DexElement elem, ValueTable table) {
        DexFunction func = (DexFunction) elem;
        for (DexParam param : func.sig().params()) {
            String name = param.paramName().toString();
            Type type = InferType.$(ts, param.paramType());
            table.define(new Value(name, type, param));
        }
    }
}
