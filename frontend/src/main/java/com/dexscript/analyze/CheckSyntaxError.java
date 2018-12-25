package com.dexscript.analyze;

import com.dexscript.ast.DexFile;
import com.dexscript.ast.core.DexElement;

public class CheckSyntaxError implements DexElement.Visitor {

    private boolean hasError;

    public CheckSyntaxError(DexElement elem) {
        visit(elem);
    }

    @Override
    public void visit(DexElement elem) {
        if (elem.parent() == null && !(elem instanceof DexFile)) {
            System.out.println("missing parent for " + elem);
            hasError = true;
        }
        if (elem.syntaxError() != null) {
            System.out.println(elem.syntaxError());
            hasError = true;
        }
        elem.walkDown(this);
    }

    public boolean hasError() {
        return hasError;
    }
}
