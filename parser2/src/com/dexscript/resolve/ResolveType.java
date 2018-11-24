package com.dexscript.resolve;

import com.dexscript.ast.expr.DexReference;

public class ResolveType {

    public static final Denotation.Type STRING_TYPE = Denotation.javaClass("string", "String");
    public static final Denotation.Type INT64_TYPE = Denotation.javaClass("int64", "Long");
    public static final DenotationTable BUILTIN_TYPES = new DenotationTable()
            .add(STRING_TYPE)
            .add(INT64_TYPE);

    private final DenotationTable builtin;

    public ResolveType(DenotationTable builtin) {
        this.builtin = builtin;
    }

    public ResolveType() {
        this(BUILTIN_TYPES);
    }

    public Denotation.Type __(DexReference ref) {
        Denotation.Type type = ref.attachmentOfType(Denotation.Type.class);
        if (type != null) {
            return type;
        }
        type = (Denotation.Type) builtin.get(ref.toString());
        if (type != null) {
            ref.attach(type);
        }
        return type;
    }
}
