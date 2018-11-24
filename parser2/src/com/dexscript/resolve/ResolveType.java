package com.dexscript.resolve;

import com.dexscript.ast.expr.DexExpr;
import com.dexscript.ast.expr.DexReference;

final class ResolveType {

    private final DenotationTable builtin;

    ResolveType(DenotationTable builtin) {
        this.builtin = builtin;
    }

    ResolveType() {
        this(BuiltinTypes.BUILTIN_TYPES);
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

    public Denotation.Type __(DexExpr expr) {
        return BuiltinTypes.STRING_TYPE;
    }
}
