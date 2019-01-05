package com.dexscript.type.core;

import java.util.*;

public class Invocation {

    private final String funcName;
    private final List<DType> typeArgs;
    private final List<DType> posArgs;
    private final List<NamedArg> namedArgs;
    private final DType retHint;
    private boolean requireImpl;
    private boolean isGlobalScope;
    private final Set<FunctionType> providedFunctions;

    public Invocation(String funcName, List<DType> typeArgs,
                      List<DType> posArgs, List<NamedArg> namedArgs,
                      DType retHint) {
        this.funcName = funcName;
        this.typeArgs = typeArgs == null ? Collections.emptyList() : typeArgs;
        this.posArgs = posArgs == null ? Collections.emptyList() : posArgs;
        this.namedArgs = namedArgs == null ? Collections.emptyList() : namedArgs;
        this.retHint = retHint;
        ArrayList<DType> allArgs = new ArrayList<>(this.posArgs);
        for (NamedArg namedArg : this.namedArgs) {
            allArgs.add(namedArg.type());
        }
        providedFunctions = new HashSet<>();
        for (DType allArg : allArgs) {
            if (allArg instanceof CompositeType) {
                providedFunctions.addAll(((CompositeType) allArg).functions());
            }
        }
    }

    // when checking semantic error, the interface function is assumed to have impl
    // when transpile, we set require impl
    public Invocation requireImpl(boolean requireImpl) {
        this.requireImpl = requireImpl;
        return this;
    }

    public boolean requireImpl() {
        return requireImpl;
    }

    public Invocation isGlobalScope(boolean isGlobalScope) {
        this.isGlobalScope = isGlobalScope;
        return this;
    }

    public boolean isGlobalScope() {
        return isGlobalScope;
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

    public boolean canProvide(FunctionType func) {
        return providedFunctions.contains(func);
    }

    public int argsCount() {
        // context argument
        return posArgs.size() + namedArgs.size() + 1;
    }

    @Override
    public String toString() {
        StringBuilder desc = new StringBuilder();
        desc.append(funcName);
        if (!typeArgs.isEmpty()) {
            desc.append('<');
            for (int i = 0; i < typeArgs.size(); i++) {
                if (i > 0) {
                    desc.append(", ");
                }
                desc.append(typeArgs.get(i).toString());
            }
            desc.append('>');
        }
        desc.append('(');
        boolean isFirst = true;
        for (DType posArg : posArgs) {
            isFirst = appendMore(desc, isFirst);
            desc.append(posArg.toString());
        }
        for (NamedArg namedArg : namedArgs) {
            isFirst = appendMore(desc, isFirst);
            desc.append(namedArg.name());
            desc.append('=');
            desc.append(namedArg.type().toString());
        }
        desc.append(")");
        if (retHint != null) {
            desc.append(": ");
            desc.append(retHint.toString());
        }
        return desc.toString();
    }

    public boolean appendMore(StringBuilder desc, boolean isFirst) {
        if (isFirst) {
            isFirst = false;
        } else {
            desc.append(", ");
        }
        return isFirst;
    }
}
