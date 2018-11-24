package com.dexscript.transpile;

import com.dexscript.ast.expr.DexReference;
import com.dexscript.resolve.Denotation;
import com.dexscript.resolve.ResolveType;
import com.dexscript.resolve.ResolveValue;

// bookkeeping of the symbols in the town
public class Township {

    public ResolveType resolveType;
    public ResolveValue resolveValue;

    public Denotation.Type resolveType(DexReference ref) {
        Denotation.Type type = ref.attachmentOfType(Denotation.Type.class);
        if (type != null) {
            return type;
        }
        type = resolveType.__(ref);
        ref.attach(type);
        return type;
    }

    public Denotation.Value resolveValue(DexReference ref) {
        return resolveValue.__(ref);
    }
}
