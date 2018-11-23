package com.dexscript.resolve;

import com.dexscript.ast.expr.DexReference;

public class ResolveType {

    public static final Denotation.Type STRING_TYPE = new Denotation.Type("string", Denotation.TypeKind.BUILTIN);
    public static final DenotationTable BUILTIN_TYPES = new DenotationTable()
            .add(STRING_TYPE);

    private final DenotationTable builtin;

    public ResolveType(DenotationTable builtin) {
        this.builtin = builtin;
    }

    public ResolveType() {
        this(BUILTIN_TYPES);
    }

    public Denotation.Type __(DexReference ref) {
        return (Denotation.Type) builtin.get(ref.toString());
    }
}
