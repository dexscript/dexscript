package com.dexscript.resolver;

import com.dexscript.ast.expr.DexReference;

public interface Resolve {
    static ResolvedSymbol __(DexReference ref) {
        String symbolName = ref.toString();
        ref.parentStmt();
    }
}
