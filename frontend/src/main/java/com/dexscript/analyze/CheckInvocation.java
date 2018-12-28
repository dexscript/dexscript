package com.dexscript.analyze;

import com.dexscript.ast.core.DexElement;
import com.dexscript.ast.expr.DexInvocation;
import com.dexscript.ast.expr.DexInvocationExpr;
import com.dexscript.infer.InferInvocation;
import com.dexscript.type.*;

public class CheckInvocation<E extends DexElement & DexInvocationExpr> implements CheckSemanticError.Handler<E> {
    @Override
    public void handle(CheckSemanticError cse, E elem) {
        TypeSystem ts = cse.typeSystem();
        DexInvocation dexIvc = elem.invocation();
        Invocation ivc = InferInvocation.$(ts, dexIvc);
        Dispatched dispatched = ts.dispatch(ivc);
        if (dispatched.candidates.isEmpty()) {
            cse.report(elem, "no matching function: " + elem);
        }
    }
}
