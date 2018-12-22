package com.dexscript.analyze;

import com.dexscript.ast.core.DexElement;
import com.dexscript.ast.expr.DexInvocation;
import com.dexscript.ast.expr.DexInvocationExpr;
import com.dexscript.infer.InferType;
import com.dexscript.type.DType;
import com.dexscript.type.Invocation;
import com.dexscript.type.ResolveType;
import com.dexscript.type.TypeSystem;

import java.util.List;

public class CheckInvocation<E extends DexElement & DexInvocationExpr> implements CheckSemanticError.Handler<E> {
    @Override
    public void handle(CheckSemanticError cse, E elem) {
        TypeSystem ts = cse.typeSystem();
        DexInvocation dexIvc = elem.invocation();
        List<DType> posArgs = InferType.inferTypes(ts, dexIvc.posArgs());
        List<DType> typeArgs = ResolveType.resolveTypes(ts, null, dexIvc.typeArgs());
        Invocation ivc = new Invocation(dexIvc.funcName(), typeArgs, posArgs, null,null);
        if (ts.invoke(ivc).candidates.isEmpty()) {
            cse.report(elem, "no matching function: " + elem);
        }
    }
}
