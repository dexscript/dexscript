package com.dexscript.analyze;

import com.dexscript.ast.type.DexTypeRef;
import com.dexscript.type.BuiltinTypes;
import com.dexscript.type.DType;

class CheckTypeRef implements CheckSemanticError.Handler<DexTypeRef> {
    @Override
    public void handle(CheckSemanticError cse, DexTypeRef elem) {
        DType type = cse.typeSystem().resolveType(elem);
        if (BuiltinTypes.UNDEFINED.equals(type)) {
            cse.report(elem, "reference type not found: " + type);
        }
    }
}
