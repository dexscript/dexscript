package com.dexscript.analyze;

import com.dexscript.ast.DexParam;
import com.dexscript.ast.DexSig;
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
        if (elem instanceof DexSig) {
            DexSig sig = (DexSig) elem;
            check(sig);
            return;
        }
        if (elem instanceof DexReference) {
            check(resolve.resolveValue((DexReference) elem));
            return;
        }
        elem.walkDown(this);
    }

    private void check(Denotation denotation) {
        if (denotation instanceof Denotation.Error) {
            Denotation.Error err = (Denotation.Error) denotation;
            System.out.println(err.message);
            hasError = true;
        }
    }

    private void check(DexSig sig) {
        for (DexParam param : sig.params()) {
            check(resolve.resolveType(param.paramType()));
        }
        if (sig.ret() != null) {
            check(resolve.resolveType(sig.ret()));
        }
    }
}
