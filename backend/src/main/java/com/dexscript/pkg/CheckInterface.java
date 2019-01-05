package com.dexscript.pkg;

import com.dexscript.ast.DexInterface;
import com.dexscript.ast.inf.DexInfTypeParam;
import com.dexscript.type.core.DType;
import com.dexscript.type.core.ResolveType;
import com.dexscript.type.core.TypeSystem;
import com.dexscript.type.core.TypeTable;

public class CheckInterface implements CheckSemanticError.Handler<DexInterface> {
    @Override
    public void handle(CheckSemanticError cse, DexInterface inf) {
        TypeSystem ts = cse.typeSystem();
        TypeTable localTypeTable = new TypeTable(ts);
        for (DexInfTypeParam typeParam : inf.typeParams()) {
            DType type = ResolveType.$(ts, null, typeParam.paramType());
            localTypeTable.define(inf.pkg(), typeParam.paramName().toString(), type);
        }
        cse.withTypeTable(localTypeTable, () -> inf.walkDown(cse));
    }
}
