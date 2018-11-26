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

    public Denotation __(DexReference ref) {
        Denotation type = ref.attachmentOfType(Denotation.Type.class);
        if (type != null) {
            return type;
        }
        String refName = ref.toString();
        type = builtin.get(refName);
        if (type == null) {
            type = new Denotation.Error(refName, "can not resolve " + refName + " to a type");
        }
        ref.attach(type);
        return type;
    }

    public Denotation.Type __(DexExpr expr) {
        return BuiltinTypes.STRING_TYPE;
    }
}
