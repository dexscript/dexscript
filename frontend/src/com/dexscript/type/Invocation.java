package com.dexscript.type;

import java.util.Collections;
import java.util.List;

public class Invocation {

    private final String funcName;
    private final List<DType> typeArgs;
    private final List<DType> posArgs;
    private final List<NamedArg> namedArgs;
    private final DType retHint;
    private boolean requireImpl;

    public Invocation(String funcName, List<DType> typeArgs, List<DType> posArgs, List<NamedArg> namedArgs, DType retHint) {
        this.funcName = funcName;
        this.typeArgs = typeArgs == null ? Collections.emptyList() : typeArgs;
        this.posArgs = posArgs == null ? Collections.emptyList() : posArgs;
        this.namedArgs = namedArgs == null ? Collections.emptyList() : namedArgs;
        this.retHint = retHint;
    }

    // when checking semantic error, the interface function is assumed to have impl
    // when transpile, we set require impl
    public Invocation requireImpl(boolean val) {
        requireImpl = val;
        return this;
    }

    public boolean requireImpl() {
        return requireImpl;
    }

    public String funcName() {
        return funcName;
    }

    public List<DType> typeArgs() {
        return typeArgs;
    }

    public List<DType> posArgs() {
        return posArgs;
    }

    public List<NamedArg> namedArgs() {
        return namedArgs;
    }

    public DType retHint() {
        return retHint;
    }
}
