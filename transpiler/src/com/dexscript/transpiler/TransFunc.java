package com.dexscript.transpiler;

import com.dexscript.psi.DexVisitor;

public class TransFunc extends DexVisitor {

    private final StringBuilder out;

    public TransFunc(StringBuilder out) {
        this.out = out;
    }

}
