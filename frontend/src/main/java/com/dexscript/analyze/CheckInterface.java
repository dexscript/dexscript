package com.dexscript.analyze;

import com.dexscript.ast.DexInterface;
import com.dexscript.ast.inf.DexInfTypeParam;
import com.dexscript.type.DType;
import com.dexscript.type.ResolveType;
import com.dexscript.type.TypeSystem;
import com.dexscript.type.TypeTable;

public class CheckInterface implements CheckSemanticError.Handler<DexInterface> {
    @Override
    public void handle(CheckSemanticError cse, DexInterface inf) {
        TypeSystem ts = cse.typeSystem();
        TypeTable localTypeTable = new TypeTable(ts);
        for (DexInfTypeParam typeParam : inf.typeParams()) {
            DType type = ResolveType.$(ts, null, typeParam.paramType());
            localTypeTable.define(inf.pkg(), typeParam.paramName().toString(), type);
        }
        cse.localTypeTable(localTypeTable);
        inf.walkDown(cse);
        cse.localTypeTable(null);
    }
}