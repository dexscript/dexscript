package com.dexscript.infer;

import com.dexscript.ast.DexActor;
import com.dexscript.ast.elem.DexParam;
import com.dexscript.type.core.ResolveType;
import com.dexscript.type.core.DType;
import com.dexscript.type.core.TypeSystem;
import com.dexscript.type.core.TypeTable;

class InferActor implements InferValue<DexActor> {

    @Override
    public void handle(TypeSystem ts, DexActor func, ValueTable table) {
        TypeTable localTypeTable = new TypeTable(ts, func.typeParams());
        for (DexParam param : func.sig().params()) {
            String name = param.paramName().toString();
            DType type = ResolveType.$(ts, localTypeTable, param.paramType());
            table.define(new Value(name, type, param));
        }
    }
}
