package com.dexscript.analyze;

import com.dexscript.ast.core.DexElement;
import com.dexscript.ast.expr.DexInvocationExpr;
import com.dexscript.type.Invocation;
import com.dexscript.type.TypeSystem;

public class CheckInvocation<E extends DexElement & DexInvocationExpr> implements CheckSemanticError.Handler<E> {
    @Override
    public void handle(CheckSemanticError cse, E elem) {
        TypeSystem ts = cse.typeSystem();
        Invocation ivc = new Invocation(ts, elem.invocation(), null);
        if (ts.invoke(ivc).candidates.isEmpty()) {
            cse.report(elem, "no matching function: " + elem);
        }
    }
}
