package com.dexscript.analyze;

import com.dexscript.ast.core.DexElement;
import com.dexscript.ast.expr.DexReference;
import com.dexscript.resolve.Denotation;
import com.dexscript.resolve.Resolve;

public class CheckSemanticError implements DexElement.Visitor {

    private final Resolve resolve;
    private boolean hasError;

    public CheckSemanticError(Resolve resolve, DexElement elem) {
        this.resolve = resolve;
        visit(elem);
    }

    public boolean hasError() {
        return hasError;
    }

    @Override
    public void visit(DexElement elem) {
        if (elem instanceof DexReference) {
            Denotation denotation = resolve.resolveValue((DexReference) elem);
            if (denotation instanceof Denotation.Error) {
                Denotation.Error err = (Denotation.Error) denotation;
                System.out.println(err.message);
                hasError = true;
            }
        } else {
            elem.walkDown(this);
        }
    }
}
