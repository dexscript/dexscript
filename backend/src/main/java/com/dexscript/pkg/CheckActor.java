package com.dexscript.pkg;

import com.dexscript.ast.DexActor;
import com.dexscript.ast.elem.DexTypeParam;
import com.dexscript.type.DType;
import com.dexscript.type.ResolveType;
import com.dexscript.type.TypeSystem;
import com.dexscript.type.TypeTable;

public class CheckActor implements CheckSemanticError.Handler<DexActor> {
    @Override
    public void handle(CheckSemanticError cse, DexActor actor) {
        TypeSystem ts = cse.typeSystem();
        TypeTable localTypeTable = new TypeTable(ts);
        for (DexTypeParam typeParam : actor.typeParams()) {
            DType type = ResolveType.$(ts, null, typeParam.paramType());
            localTypeTable.define(actor.pkg(), typeParam.paramName().toString(), type);
        }
        cse.withTypeTable(localTypeTable, () -> actor.walkDown(cse));
    }
}
