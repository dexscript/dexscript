package com.dexscript.pkg;

import com.dexscript.ast.DexActor;
import com.dexscript.ast.elem.DexTypeParam;
import com.dexscript.type.core.*;

public class CheckActor implements CheckSemanticError.Handler<DexActor> {
    @Override
    public void handle(CheckSemanticError cse, DexActor actor) {
        TypeSystem ts = cse.typeSystem();
        TypeTable localTypeTable = new TypeTable(ts);
        for (DexTypeParam typeParam : actor.typeParams()) {
            DType type = InferType.$(ts, typeParam.paramType());
            localTypeTable.define(actor.pkg(), typeParam.paramName().toString(), type);
        }
        cse.withTypeTable(localTypeTable, () -> actor.walkDown(cse));
    }
}
