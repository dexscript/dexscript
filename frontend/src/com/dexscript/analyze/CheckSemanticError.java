package com.dexscript.analyze;

import com.dexscript.ast.DexFile;
import com.dexscript.ast.core.DexElement;
import com.dexscript.type.JavaSuperTypeArgs;
import com.dexscript.type.TypeSystem;

import java.util.HashMap;
import java.util.Map;

public class CheckSemanticError implements DexElement.Visitor {

    public interface Handler<E extends DexElement> {

        void handle(CheckSemanticError cse, E elem);
    }

    private static Map<Class<? extends DexElement>, Handler> handlers = new HashMap<Class<? extends DexElement>, Handler>() {
        {
            add(new CheckValueRef());
            add(new CheckTypeRef());
            add(new CheckReturn());
        }

        private void add(Handler<?> handler) {
            put((Class<? extends DexElement>) JavaSuperTypeArgs.$(handler.getClass())[0], handler);
        }
    };

    private final TypeSystem ts;
    private boolean hasError;

    public CheckSemanticError(TypeSystem ts, DexFile file) {
        this.ts = ts;
        visit(file);
    }

    @Override
    public void visit(DexElement elem) {
        Handler handler = handlers.get(elem.getClass());
        if (handler == null) {
            elem.walkDown(this);
        } else {
            handler.handle(this, elem);
        }
    }

    public boolean hasError() {
        return hasError;
    }

    public void report(DexElement occuredAt, String error) {
        System.out.println(error);
        hasError = true;
    }

    public TypeSystem typeSystem() {
        return ts;
    }

//    private final ResolveType handle;
//    private boolean hasError;
//
//    public CheckSemanticError(ResolveType handle, DexElement body) {
//        this.handle = handle;
//        visit(body);
//    }
//
//    public boolean hasError() {
//        return hasError;
//    }
//
//    @Override
//    public void visit(DexElement body) {
//        if (body instanceof DexSig) {
//            DexSig sig = (DexSig) body;
//            check(sig);
//            return;
//        }
//        if (body instanceof DexValueRef) {
//            notError(handle.resolveValue((DexValueRef) body));
//            return;
//        }
//        if (body instanceof DexReturnStmt) {
//            check((DexReturnStmt) body);
//            return;
//        }
//        if (body instanceof DexFunctionCallExpr) {
//            check((DexFunctionCallExpr)body);
//            return;
//        }
//        if (body instanceof DexMethodCallExpr) {
//            check((DexMethodCallExpr)body);
//            return;
//        }
//        if (body instanceof DexNewExpr) {
//            check((DexNewExpr)body);
//            return;
//        }
//        body.walkDown(this);
//    }
//
//    private void check(DexNewExpr body) {
//        for (DexExpr arg : body.args()) {
//            visit(arg);
//        }
//        notError(handle.resolveType(body));
//    }
//
//    private void check(DexMethodCallExpr body) {
//        visit(body.obj());
//        for (DexExpr arg : body.args()) {
//            visit(arg);
//        }
//        notError(handle.resolveType(body));
//    }
//
//    private void check(DexFunctionCallExpr body) {
//        for (DexExpr arg : body.args()) {
//            visit(arg);
//        }
//        notError(handle.resolveType(body));
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
//        Denotation exprType = handle.resolveType(expr);
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
//        Denotation paramType = handle.resolveType(sig.ret());
//        if (paramType instanceof Type) {
//            return (Type) paramType;
//        }
//        return BuiltinTypes.UNDEFINED_TYPE;
//    }
//
//    private boolean notError(Denotation paramType) {
//        if (paramType instanceof Denotation.Error) {
//            Denotation.Error err = (Denotation.Error) paramType;
//            System.out.println(err.toString());
//            hasError = true;
//            return false;
//        }
//        return true;
//    }
//
//    private void check(DexSig sig) {
//        for (DexParam param : sig.params()) {
//            notError(handle.resolveType(param.paramType()));
//        }
//        if (sig.ret() != null) {
//            notError(handle.resolveType(sig.ret()));
//        }
//    }
}
