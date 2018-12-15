package com.dexscript.analyze;

import com.dexscript.ast.stmt.DexAssignStmt;
import com.dexscript.infer.InferType;
import com.dexscript.type.Type;
import com.dexscript.type.TypeSystem;

public class CheckAssignment implements CheckSemanticError.Handler<DexAssignStmt> {

    @Override
    public void handle(CheckSemanticError cse, DexAssignStmt elem) {
        if (elem.targets().size() != 1) {
            throw new UnsupportedOperationException("not implemented");
        }
        TypeSystem ts = cse.typeSystem();
        Type left = InferType.$(ts, elem.targets().get(0));
        Type right = InferType.$(ts, elem.expr());
        if (!left.isAssignableFrom(right)) {
            cse.report(elem, left + " is not assignable from " + right);
        }
    }
}
