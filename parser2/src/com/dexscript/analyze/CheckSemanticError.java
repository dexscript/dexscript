package com.dexscript.analyze;

import com.dexscript.ast.core.DexElement;

public class CheckSemanticError {

    private boolean hasError;

    public CheckSemanticError(DexElement elem) {

    }

    public boolean hasError() {
        return hasError;
    }
}
