package com.dexscript.type;

import com.dexscript.ast.expr.DexInvocation;
import com.dexscript.infer.InferType;

import java.util.List;

public class Invocation {

    private final String funcName;
    private final List<Type> typeArgs;
    private final List<Type> args;
    private final Type retHint;

    public Invocation(String funcName, List<Type> typeArgs, List<Type> args, Type retHint) {
        this.funcName = funcName;
        this.typeArgs = typeArgs;
        this.args = args;
        this.retHint = retHint;
    }

    public Invocation(TypeSystem ts, DexInvocation ivc, Type retHint) {
        List<Type> args = InferType.inferTypes(ts, ivc.args());
        List<Type> typeArgs = ts.resolveTypes(ivc.typeArgs());
        this.funcName = ivc.funcName();
        this.typeArgs = typeArgs;
        this.args = args;
        this.retHint = retHint;
    }

    public String funcName() {
        return funcName;
    }

    public List<Type> typeArgs() {
        return typeArgs;
    }

    public List<Type> args() {
        return args;
    }

    public Type retHint() {
        return retHint;
    }
}
