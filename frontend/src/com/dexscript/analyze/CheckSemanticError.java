package com.dexscript.analyze;

import com.dexscript.ast.DexFile;
import com.dexscript.ast.core.DexElement;
import com.dexscript.ast.expr.DexFunctionCallExpr;
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
            add(new CheckInvocation<DexFunctionCallExpr>() {
            });
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
}
