package com.dexscript.resolve;

import com.dexscript.ast.expr.DexReference;

import java.util.List;

public interface ResolveVar {

    static List<Denotation> __(DexReference ref) {
        return null;
    }
}
