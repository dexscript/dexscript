package com.dexscript.ast.expr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DexInvocation {

    private final String funcName;
    private final List<DexExpr> args;

    public DexInvocation(String funcName, List<DexExpr> args) {
        this.funcName = funcName;
        this.args = args;
    }

    public DexInvocation(String funcName, DexExpr arg0, DexExpr arg1) {
        this.funcName = funcName;
        this.args = Arrays.asList(arg0, arg1);
    }

    public DexInvocation(String funcName, DexExpr arg0, List<DexExpr> args) {
        this.funcName = funcName;
        this.args = new ArrayList<>();
        this.args.add(arg0);
        this.args.addAll(args);
    }

    public String funcName() {
        return funcName;
    }

    public List<DexExpr> args() {
        return args;
    }
}
