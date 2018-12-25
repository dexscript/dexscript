package com.dexscript.ast.expr;

import com.dexscript.ast.elem.DexIdentifier;

public class DexNamedArg {

    private final DexIdentifier name;
    private final DexExpr val;

    public DexNamedArg(DexIdentifier name, DexExpr val) {
        this.name = name;
        this.val = val;
    }

    public DexIdentifier name() {
        return name;
    }

    public DexExpr val() {
        return val;
    }
}
