package com.dexscript.pkg;

import com.dexscript.ast.core.DexElement;
import com.dexscript.ast.expr.*;
import com.dexscript.ast.stmt.DexAssignStmt;
import com.dexscript.type.core.InferType;
import com.dexscript.type.core.DType;
import com.dexscript.type.core.IsAssignable;
import com.dexscript.type.core.TypeSystem;

public class CheckAssignment implements CheckSemanticError.Handler<DexAssignStmt> {

    @Override
    public void handle(CheckSemanticError cse, DexAssignStmt elem) {
        if (elem.targets().size() != 1) {
            throw new UnsupportedOperationException("not implemented");
        }
        if (elem.isInvokable()) {
            CheckInvocation.$(cse, elem);
            return;
        }
        DexExpr leftExpr = elem.targets().get(0);
        if (!(leftExpr instanceof DexValueRef)) {
            cse.report(elem, "is not left value: " + leftExpr);
            return;
        }
        TypeSystem ts = cse.typeSystem();
        DType left = InferType.$(ts, leftExpr);
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
