package com.dexscript.resolve;

import com.dexscript.ast.core.DexElement;

public interface DexSemanticError {
    DexElement occurredAt();
}
