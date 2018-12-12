package com.dexscript.infer;

import com.dexscript.ast.expr.DexExpr;
import com.dexscript.ast.expr.DexInvocation;
import com.dexscript.ast.expr.DexInvocationExpr;
import com.dexscript.type.ResolveReturnType;
import com.dexscript.type.Type;
import com.dexscript.type.TypeSystem;

import java.util.List;

public class InferInvocation<E extends DexExpr & DexInvocationExpr> implements InferType<E> {
    @Override
    public Type handle(TypeSystem ts, E elem) {
        DexInvocation invocation = elem.invocation();
        List<Type> args = InferType.inferTypes(ts, invocation.args());
        return ResolveReturnType.$(ts, invocation.funcName(), null, args, null);
    }
}
