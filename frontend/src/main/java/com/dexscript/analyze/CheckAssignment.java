package com.dexscript.analyze;

import com.dexscript.ast.core.DexElement;
import com.dexscript.ast.stmt.DexAssignStmt;
import com.dexscript.infer.InferType;
import com.dexscript.type.DType;
import com.dexscript.type.IsAssignable;
import com.dexscript.type.TypeSystem;

public class CheckAssignment implements CheckSemanticError.Handler<DexAssignStmt> {

    @Override
    public void handle(CheckSemanticError cse, DexAssignStmt elem) {
        if (elem.targets().size() != 1) {
            throw new UnsupportedOperationException("not implemented");
        }
        TypeSystem ts = cse.typeSystem();
        DType left = InferType.$(ts, elem.targets().get(0));
        DType right = InferType.$(ts, elem.expr());
        checkTypeAssignable(cse, elem, right, left);
    }

    public static void checkTypeAssignable(CheckSemanticError cse, DexElement elem, DType from, DType to) {
        IsAssignable isAssignable = new IsAssignable(to, from);
        if (!isAssignable.result()) {
            cse.report(elem, to + " is not assignable from " + from);
            isAssignable.dump();
        }
    }
}
