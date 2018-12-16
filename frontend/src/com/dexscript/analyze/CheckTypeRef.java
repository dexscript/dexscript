package com.dexscript.analyze;

import com.dexscript.ast.type.DexTypeRef;
import com.dexscript.type.DType;
import com.dexscript.type.ResolveType;
import com.dexscript.type.TypeSystem;

class CheckTypeRef implements CheckSemanticError.Handler<DexTypeRef> {
    @Override
    public void handle(CheckSemanticError cse, DexTypeRef elem) {
        TypeSystem ts = cse.typeSystem();
        DType type = ResolveType.$(ts, null, elem);
        if (ts.UNDEFINED.equals(type)) {
            cse.report(elem, "reference type not found: " + type);
        }
    }
}
