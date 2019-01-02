package com.dexscript.analyze;

import com.dexscript.ast.core.DexElement;
import com.dexscript.ast.expr.DexExpr;
import com.dexscript.ast.expr.DexIndexExpr;
import com.dexscript.ast.expr.DexValueRef;
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
        DexExpr leftExpr = elem.targets().get(0);
        boolean isLeftValue = leftExpr instanceof DexValueRef || leftExpr instanceof DexIndexExpr;
        if (!isLeftValue) {
            cse.report(elem, "is not left value: " + leftExpr);
        }
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
