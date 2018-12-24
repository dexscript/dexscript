package com.dexscript.infer;

import com.dexscript.ast.core.DexSyntaxException;
import com.dexscript.ast.expr.DexExpr;
import com.dexscript.ast.expr.DexInvocation;
import com.dexscript.ast.expr.DexInvocationExpr;
import com.dexscript.ast.expr.DexNamedArg;
import com.dexscript.type.*;

import java.util.ArrayList;
import java.util.List;

public class InferInvocation<E extends DexExpr & DexInvocationExpr> implements InferType<E> {

    @Override
    public DType handle(TypeSystem ts, E elem) {
        DexInvocation dexIvc = elem.invocation();
        Invocation ivc = InferInvocation.$(ts, dexIvc);
        Invoked invoked = ts.invoke(ivc);
        return invoked.ret == null ? ts.UNDEFINED : invoked.ret;
    }

    public static Invocation $(TypeSystem ts, DexInvocation dexIvc) {
        List<DType> posArgs = InferType.inferTypes(ts, dexIvc.posArgs());
        List<DType> typeArgs = ResolveType.resolveTypes(ts, null, dexIvc.typeArgs());
        List<NamedArg> namedArgs = new ArrayList<>();
        DType context = null;
        for (DexNamedArg dexNamedArg : dexIvc.namedArgs()) {
            String argName = dexNamedArg.name().toString();
            DType argType = InferType.$(ts, dexNamedArg.val());
            if (argName.equals("$")) {
                context = argType;
            } else {
                if (context != null) {
                    throw new DexSyntaxException("context argument $ must be the last argument");
                }
                namedArgs.add(new NamedArg(argName, argType));
            }
        }
        if (context == null) {
            context = ts.context(dexIvc.pkg());
        }
        if (ts.isConst(context)) {
            throw new DexSyntaxException("context argument $ must not be const value");
        }
        Invocation ivc = new Invocation(dexIvc.funcName(), typeArgs, posArgs, namedArgs, context, null);
        return ivc;
    }
}
