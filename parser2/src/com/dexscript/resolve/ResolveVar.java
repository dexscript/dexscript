package com.dexscript.resolve;

import com.dexscript.parser2.expr.DexReference;

import java.util.List;

public interface ResolveVar {

    static List<Denotation> __(DexReference ref) {
        return null;
    }
}
