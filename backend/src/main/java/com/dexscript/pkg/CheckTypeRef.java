package com.dexscript.pkg;

import com.dexscript.ast.type.DexTypeRef;
import com.dexscript.type.core.DType;
import com.dexscript.type.core.ResolveType;
import com.dexscript.type.core.TypeSystem;

class CheckTypeRef implements CheckSemanticError.Handler<DexTypeRef> {
    @Override
    public void handle(CheckSemanticError cse, DexTypeRef elem) {
        TypeSystem ts = cse.typeSystem();
        DType type = ResolveType.$(ts, cse.localTypeTable(), elem);
        if (ts.UNDEFINED.equals(type)) {
            cse.report(elem, "reference type not found: " + type);
        }
    }
}
