package com.dexscript.analyze;

import com.dexscript.ast.DexParam;
import com.dexscript.ast.DexSig;
import com.dexscript.ast.core.DexElement;
import com.dexscript.ast.expr.DexCallExpr;
import com.dexscript.ast.expr.DexExpr;
import com.dexscript.ast.expr.DexReference;
import com.dexscript.ast.stmt.DexReturnStmt;
import com.dexscript.resolve.BuiltinTypes;
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
            check((DexReturnStmt) elem);
            return;
        }
        if (elem instanceof DexCallExpr) {
            check((DexCallExpr)elem);
            return;
        }
        elem.walkDown(this);
    }

    private void check(DexCallExpr elem) {
        for (DexExpr arg : elem.args()) {
            visit(arg);
        }
        notError(resolve.resolveFunction(elem.target().asRef()));
    }

    private void check(DexReturnStmt returnStmt) {
        Denotation.Type exprType = getExprType(returnStmt.expr());
        Denotation.Type returnType = getReturnType(returnStmt.sig());
        if (!returnType.assignableFrom(exprType)) {
            TypeIncompatibleError err = new TypeIncompatibleError(returnStmt, returnType, exprType);
            System.out.println(err);
            returnStmt.attach(err);
            hasError = true;
        }
        visit(returnStmt.expr());
    }

    private Denotation.Type getExprType(DexExpr expr) {
        Denotation exprType = resolve.resolveType(expr);
        if (exprType instanceof Denotation.Type) {
            return (Denotation.Type) exprType;
        }
        return BuiltinTypes.UNDEFINED_TYPE;
    }

    private Denotation.Type getReturnType(DexSig sig) {
        if (sig == null) {
            return BuiltinTypes.UNDEFINED_TYPE;
        }
        if (sig.ret() == null) {
            return BuiltinTypes.UNDEFINED_TYPE;
        }
        Denotation denotation = resolve.resolveType(sig.ret());
        if (denotation instanceof Denotation.Type) {
            return (Denotation.Type) denotation;
        }
        return BuiltinTypes.UNDEFINED_TYPE;
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
