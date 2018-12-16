package com.dexscript.infer;

import com.dexscript.ast.expr.DexExpr;
import com.dexscript.ast.expr.DexInvocation;
import com.dexscript.ast.expr.DexInvocationExpr;
import com.dexscript.type.DType;
import com.dexscript.type.ResolveReturnType;
import com.dexscript.type.ResolveType;
import com.dexscript.type.TypeSystem;

import java.util.List;

public class InferInvocation<E extends DexExpr & DexInvocationExpr> implements InferType<E> {
    @Override
    public DType handle(TypeSystem ts, E elem) {
        DexInvocation invocation = elem.invocation();
        List<DType> args = InferType.inferTypes(ts, invocation.args());
        List<DType> typeArgs = ResolveType.resolveTypes(ts, null, invocation.typeArgs());
        return ResolveReturnType.$(ts, invocation.funcName(), typeArgs, args, null);
    }
}
