package com.dexscript.analyze;

import com.dexscript.ast.DexParam;
import com.dexscript.ast.DexSig;
import com.dexscript.ast.core.DexElement;
import com.dexscript.ast.expr.DexReference;
import com.dexscript.ast.stmt.DexReturnStmt;
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
            notError(resolve.resolveValue((DexReference) elem));
            return;
        }
        if (elem instanceof DexReturnStmt) {
            check((DexReturnStmt)elem);
        }
        elem.walkDown(this);
    }

    private void check(DexReturnStmt returnStmt) {
        Denotation exprType = resolve.resolveType(returnStmt.expr());
        if (!notError(exprType)) {
            return;
        }
        DexSig sig = returnStmt.sig();
        if (sig == null) {
        }
    }

    private boolean notError(Denotation denotation) {
        if (denotation instanceof Denotation.Error) {
            Denotation.Error err = (Denotation.Error) denotation;
            System.out.println(err.toString());
            hasError = true;
            return false;
        }
        return true;
    }

    private void check(DexSig sig) {
        for (DexParam param : sig.params()) {
            notError(resolve.resolveType(param.paramType()));
        }
        if (sig.ret() != null) {
            notError(resolve.resolveType(sig.ret()));
        }
    }
}
