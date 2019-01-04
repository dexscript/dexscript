package com.dexscript.analyze;

import com.dexscript.ast.core.DexElement;
import com.dexscript.ast.expr.DexExpr;
import com.dexscript.ast.expr.DexInvocation;
import com.dexscript.ast.expr.DexInvocationExpr;
import com.dexscript.ast.expr.DexNamedArg;
import com.dexscript.ast.type.DexType;
import com.dexscript.infer.InferInvocation;
import com.dexscript.type.Dispatched;
import com.dexscript.type.Invocation;
import com.dexscript.type.TypeSystem;

public class CheckInvocation<E extends DexElement & DexInvocationExpr> implements CheckSemanticError.Handler<E> {

    @Override
    public void handle(CheckSemanticError cse, E elem) {
        CheckInvocation.$(cse, elem);
    }

    public static void $(CheckSemanticError cse, DexInvocationExpr elem) {
        if (!elem.isInvokable()) {
            return;
        }
        $(cse, (DexElement) elem, elem.invocation());
    }

    public static void $(CheckSemanticError cse, DexElement elem, DexInvocation dexIvc) {
        TypeSystem ts = cse.typeSystem();
        for (DexType typeArg : dexIvc.typeArgs()) {
            cse.visit(typeArg);
        }
        for (DexExpr posArg : dexIvc.posArgs()) {
            cse.visit(posArg);
        }
        for (DexNamedArg namedArg : dexIvc.namedArgs()) {
            cse.visit(namedArg.val());
        }
        Invocation ivc = InferInvocation.ivc(ts, dexIvc);
        Dispatched dispatched = ts.dispatch(ivc);
        if (dispatched.candidates.isEmpty()) {
            cse.report(elem, "no matching function: " + ivc);
            System.out.println("dispatched.failures: " + dispatched.failures.size());
            System.out.println("dispatched.ignoreds: " + dispatched.ignoreds.size());
            System.out.println("dispatched.skippeds: " + dispatched.skippeds.size());
        }
    }
}
