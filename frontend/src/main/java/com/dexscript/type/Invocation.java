package com.dexscript.type;

import java.util.Collections;
import java.util.List;

public class Invocation {

    private final String funcName;
    private final List<DType> typeArgs;
    private final List<DType> posArgs;
    private final List<NamedArg> namedArgs;
    private final DType context;
    private final DType retHint;
    private boolean requireImpl;

    public Invocation(String funcName, List<DType> typeArgs,
                      List<DType> posArgs, List<NamedArg> namedArgs, DType context,
                      DType retHint) {
        this.funcName = funcName;
        this.typeArgs = typeArgs == null ? Collections.emptyList() : typeArgs;
        this.posArgs = posArgs == null ? Collections.emptyList() : posArgs;
        this.namedArgs = namedArgs == null ? Collections.emptyList() : namedArgs;
        this.context = context;
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

    public DType context() {
        return context;
    }

    public DType retHint() {
        return retHint;
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
        isFirst = appendMore(desc, isFirst);
        desc.append("$=");
        desc.append(context.toString());
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
