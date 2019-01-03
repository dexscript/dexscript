package com.dexscript.ast.expr;

import com.dexscript.ast.DexPackage;
import com.dexscript.ast.type.DexType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DexInvocation {

    private final DexPackage pkg;
    private final String funcName;
    private final List<DexType> typeArgs;
    private final List<DexExpr> posArgs;
    private final List<DexNamedArg> namedArgs;
    private boolean isGlobalScope;

    public DexInvocation(DexPackage pkg, String funcName, List<DexExpr> posArgs) {
        this.pkg = pkg;
        this.funcName = funcName;
        this.typeArgs = Collections.emptyList();
        this.posArgs = posArgs;
        this.namedArgs = Collections.emptyList();
    }

    public DexInvocation(DexPackage pkg, String funcName, DexExpr arg0, DexExpr arg1) {
        this.pkg = pkg;
        this.funcName = funcName;
        this.typeArgs = Collections.emptyList();
        this.posArgs = Arrays.asList(arg0, arg1);
        this.namedArgs = Collections.emptyList();
    }

    public DexInvocation(DexPackage pkg, String funcName, DexExpr arg0, List<DexType> typeArgs, List<DexExpr> posArgs) {
        this.pkg = pkg;
        this.funcName = funcName;
        this.typeArgs = typeArgs;
        this.posArgs = new ArrayList<>();
        this.posArgs.add(arg0);
        this.posArgs.addAll(posArgs);
        this.namedArgs = Collections.emptyList();
    }

    public DexInvocation(DexPackage pkg, String funcName, List<DexType> typeArgs, List<DexExpr> posArgs, List<DexNamedArg> namedArgs) {
        this.pkg = pkg;
        this.funcName = funcName;
        this.typeArgs = typeArgs;
        this.posArgs = posArgs;
        this.namedArgs = namedArgs;
    }

    public DexInvocation(DexPackage pkg, String funcName, DexExpr arg0, List<DexType> typeArgs, List<DexExpr> posArgs, List<DexNamedArg> namedArgs) {
        this.pkg = pkg;
        this.funcName = funcName;
        this.typeArgs = typeArgs;
        this.posArgs = new ArrayList<>();
        this.posArgs.add(arg0);
        this.posArgs.addAll(posArgs);
        this.namedArgs = namedArgs;
    }

    public DexPackage pkg() {
        return pkg;
    }

    public String funcName() {
        return funcName;
    }

    public List<DexExpr> posArgs() {
        return posArgs;
    }

    public List<DexType> typeArgs() {
        return typeArgs;
    }

    public List<DexNamedArg> namedArgs() {
        return namedArgs;
    }

    public DexExpr context() {
        if (namedArgs.isEmpty()) {
            return null;
        }
        DexNamedArg lastNamedArg = namedArgs.get(namedArgs.size() - 1);
        if (!lastNamedArg.name().toString().equals("$")) {
            return null;
        }
        return lastNamedArg.val();
    }

    public DexInvocation isGlobalScope(boolean isGlobalScope) {
        this.isGlobalScope = isGlobalScope;
        return this;
    }

    public boolean isGlobalScope() {
        return isGlobalScope;
    }
}
