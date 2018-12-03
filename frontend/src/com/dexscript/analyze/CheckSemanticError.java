package com.dexscript.analyze;

import com.dexscript.ast.core.DexElement;

public class CheckSemanticError implements DexElement.Visitor {
    @Override
    public void visit(DexElement elem) {

    }

//    private final Resolve resolve;
//    private boolean hasError;
//
//    public CheckSemanticError(Resolve resolve, DexElement elem) {
//        this.resolve = resolve;
//        visit(elem);
//    }
//
//    public boolean hasError() {
//        return hasError;
//    }
//
//    @Override
//    public void visit(DexElement elem) {
//        if (elem instanceof DexSig) {
//            DexSig sig = (DexSig) elem;
//            check(sig);
//            return;
//        }
//        if (elem instanceof DexReference) {
//            notError(resolve.resolveValue((DexReference) elem));
//            return;
//        }
//        if (elem instanceof DexReturnStmt) {
//            check((DexReturnStmt) elem);
//            return;
//        }
//        if (elem instanceof DexFunctionCallExpr) {
//            check((DexFunctionCallExpr)elem);
//            return;
//        }
//        if (elem instanceof DexMethodCallExpr) {
//            check((DexMethodCallExpr)elem);
//            return;
//        }
//        if (elem instanceof DexNewExpr) {
//            check((DexNewExpr)elem);
//            return;
//        }
//        elem.walkDown(this);
//    }
//
//    private void check(DexNewExpr elem) {
//        for (DexExpr arg : elem.args()) {
//            visit(arg);
//        }
//        notError(resolve.resolveType(elem));
//    }
//
//    private void check(DexMethodCallExpr elem) {
//        visit(elem.obj());
//        for (DexExpr arg : elem.args()) {
//            visit(arg);
//        }
//        notError(resolve.resolveType(elem));
//    }
//
//    private void check(DexFunctionCallExpr elem) {
//        for (DexExpr arg : elem.args()) {
//            visit(arg);
//        }
//        notError(resolve.resolveType(elem));
//    }
//
//    private void check(DexReturnStmt returnStmt) {
//        Type exprType = getExprType(returnStmt.expr());
//        Type returnType = getReturnType(returnStmt.sig());
//        if (!returnType.isAssignableFrom(exprType)) {
//            TypeIncompatibleError err = new TypeIncompatibleError(returnStmt, returnType, exprType);
//            System.out.println(err);
//            returnStmt.attach(err);
//            hasError = true;
//        }
//        visit(returnStmt.expr());
//    }
//
//    private Type getExprType(DexExpr expr) {
//        Denotation exprType = resolve.resolveType(expr);
//        if (exprType instanceof Type) {
//            return (Type) exprType;
//        }
//        return BuiltinTypes.UNDEFINED_TYPE;
//    }
//
//    private Type getReturnType(DexSig sig) {
//        if (sig == null) {
//            return BuiltinTypes.UNDEFINED_TYPE;
//        }
//        if (sig.ret() == null) {
//            return BuiltinTypes.UNDEFINED_TYPE;
//        }
//        Denotation denotation = resolve.resolveType(sig.ret());
//        if (denotation instanceof Type) {
//            return (Type) denotation;
//        }
//        return BuiltinTypes.UNDEFINED_TYPE;
//    }
//
//    private boolean notError(Denotation denotation) {
//        if (denotation instanceof Denotation.Error) {
//            Denotation.Error err = (Denotation.Error) denotation;
//            System.out.println(err.toString());
//            hasError = true;
//            return false;
//        }
//        return true;
//    }
//
//    private void check(DexSig sig) {
//        for (DexParam param : sig.params()) {
//            notError(resolve.resolveType(param.paramType()));
//        }
//        if (sig.ret() != null) {
//            notError(resolve.resolveType(sig.ret()));
//        }
//    }
}