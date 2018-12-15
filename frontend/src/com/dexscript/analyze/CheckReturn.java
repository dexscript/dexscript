package com.dexscript.analyze;

import com.dexscript.ast.stmt.DexReturnStmt;
import com.dexscript.infer.InferType;
import com.dexscript.type.BuiltinTypes;
import com.dexscript.type.Type;

class CheckReturn implements CheckSemanticError.Handler<DexReturnStmt> {
    @Override
    public void handle(CheckSemanticError cse, DexReturnStmt elem) {
        Type actualType = InferType.$(cse.typeSystem(), elem.expr());
        if (BuiltinTypes.UNDEFINED.equals(actualType)) {
            cse.report(elem.expr(), "referenced value not found: " + elem);
            return;
        }
        Type expectedType = cse.typeSystem().resolveType(elem.sig().ret());
        if (!expectedType.isAssignableFrom(actualType)) {
            cse.report(elem, "returned type not assignable, expected: " + expectedType + ", actual: " + actualType);
            return;
        }
    }
}
