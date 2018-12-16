package com.dexscript.analyze;

import com.dexscript.ast.stmt.DexReturnStmt;
import com.dexscript.infer.InferType;
import com.dexscript.type.BuiltinTypes;
import com.dexscript.type.DType;

class CheckReturn implements CheckSemanticError.Handler<DexReturnStmt> {
    @Override
    public void handle(CheckSemanticError cse, DexReturnStmt elem) {
        DType assignedFrom = InferType.$(cse.typeSystem(), elem.expr());
        if (BuiltinTypes.UNDEFINED.equals(assignedFrom)) {
            cse.report(elem.expr(), "referenced value not found: " + elem);
            return;
        }
        DType assignedTo = cse.typeSystem().resolveType(elem.sig().ret());
        CheckAssignment.checkTypeAssignable(cse, elem, assignedTo, assignedFrom);
    }
}
