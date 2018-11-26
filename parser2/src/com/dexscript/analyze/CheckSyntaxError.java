package com.dexscript.analyze;

import com.dexscript.ast.core.DexElement;

public class CheckSyntaxError implements DexElement.Visitor {

    private boolean hasError;

    public CheckSyntaxError(DexElement elem) {
        visit(elem);
    }

    @Override
    public void visit(DexElement elem) {
        if (elem.syntaxError() != null) {
            hasError = true;
        }
        elem.walkDown(this);
    }

    public boolean hasError() {
        return hasError;
    }
}
