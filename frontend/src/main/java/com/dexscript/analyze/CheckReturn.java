package com.dexscript.analyze;

import com.dexscript.ast.stmt.DexReturnStmt;
import com.dexscript.infer.InferType;
import com.dexscript.type.DType;
import com.dexscript.type.ResolveType;
import com.dexscript.type.TypeSystem;

class CheckReturn implements CheckSemanticError.Handler<DexReturnStmt> {
    @Override
    public void handle(CheckSemanticError cse, DexReturnStmt elem) {
        elem.walkDown(cse);
        TypeSystem ts = cse.typeSystem();
        DType from = InferType.$(ts, elem.expr());
        if (ts.UNDEFINED.equals(from)) {
            cse.report(elem.expr(), "referenced value not found: " + elem);
            return;
        }
        DType to = ResolveType.$(ts, cse.localTypeTable(), elem.sig().ret());
        CheckAssignment.checkTypeAssignable(cse, elem, from, to);
    }
}
