package com.dexscript.analyze;

import com.dexscript.ast.core.DexElement;
import com.dexscript.ast.stmt.DexAssignStmt;
import com.dexscript.infer.InferType;
import com.dexscript.type.DType;
import com.dexscript.type.TypeComparisonContext;
import com.dexscript.type.TypeSystem;

import java.util.ArrayList;
import java.util.HashMap;

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
        int logUntilLevelN = 4;
        ArrayList<String> logs = new ArrayList<>();
        TypeComparisonContext ctx = new TypeComparisonContext(new HashMap<>(), logUntilLevelN, logs);
        if (!to.isAssignableFrom(ctx, from)) {
            cse.report(elem, to.description() + " is not assignable from " + from.description());
            for (String log : logs) {
                System.out.println(log);
            }
        }
    }
}
