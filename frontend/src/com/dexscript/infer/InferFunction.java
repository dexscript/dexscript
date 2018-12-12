package com.dexscript.infer;

import com.dexscript.ast.DexActor;
import com.dexscript.ast.core.DexElement;
import com.dexscript.ast.elem.DexParam;
import com.dexscript.ast.elem.DexTypeParam;
import com.dexscript.type.ResolveType;
import com.dexscript.type.Type;
import com.dexscript.type.TypeSystem;
import com.dexscript.type.TypeTable;

import java.util.HashMap;
import java.util.Map;

class InferFunction implements InferValue<DexActor> {

    @Override
    public void handle(TypeSystem ts, DexActor func, ValueTable table) {
        TypeTable localTypeTable = new TypeTable(ts.typeTable());
        for (DexTypeParam typeParam : func.typeParams()) {
            localTypeTable.define(typeParam.paramName().toString(), ts.resolveType(typeParam.paramType()));
        }
        for (DexParam param : func.sig().params()) {
            String name = param.paramName().toString();
            Type type = ResolveType.$(localTypeTable, param.paramType());
            table.define(new Value(name, type, param));
        }
    }
}
