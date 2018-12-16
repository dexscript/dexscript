package com.dexscript.type;

import com.dexscript.ast.expr.DexInvocation;
import com.dexscript.infer.InferType;

import java.util.List;

public class Invocation {

    private final String funcName;
    private final List<DType> typeArgs;
    private final List<DType> args;
    private final DType retHint;

    public Invocation(String funcName, List<DType> typeArgs, List<DType> args, DType retHint) {
        this.funcName = funcName;
        this.typeArgs = typeArgs;
        this.args = args;
        this.retHint = retHint;
    }

    public Invocation(TypeSystem ts, DexInvocation ivc, DType retHint) {
        List<DType> args = InferType.inferTypes(ts, ivc.args());
        List<DType> typeArgs = ts.resolveTypes(ivc.typeArgs());
        this.funcName = ivc.funcName();
        this.typeArgs = typeArgs;
        this.args = args;
        this.retHint = retHint;
    }

    public String funcName() {
        return funcName;
    }

    public List<DType> typeArgs() {
        return typeArgs;
    }

    public List<DType> args() {
        return args;
    }

    public DType retHint() {
        return retHint;
    }
}
