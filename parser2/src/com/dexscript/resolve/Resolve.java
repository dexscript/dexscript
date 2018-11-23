package com.dexscript.resolve;

import com.dexscript.ast.expr.DexReference;

public class Resolve {

    public ResolveType resolveType;
    public ResolveValue resolveValue;

    public Denotation.Type type(DexReference ref) {
        return resolveType.__(ref);
    }

    public Denotation.Value value(DexReference ref) {
        return resolveValue.__(ref);
    }
}
