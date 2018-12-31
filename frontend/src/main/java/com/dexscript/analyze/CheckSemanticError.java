package com.dexscript.analyze;

import com.dexscript.ast.DexFile;
import com.dexscript.ast.core.DexElement;
import com.dexscript.ast.expr.DexFunctionCallExpr;
import com.dexscript.ast.expr.DexMethodCallExpr;
import com.dexscript.ast.expr.DexNewExpr;
import com.dexscript.type.JavaSuperTypeArgs;
import com.dexscript.type.TypeSystem;
import com.dexscript.type.TypeTable;

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
            add(new CheckAssignment());
            add(new CheckInterface());
            add(new CheckActor());
            add(new CheckInvocation<DexFunctionCallExpr>() {
            });
            add(new CheckInvocation<DexMethodCallExpr>() {
            });
            add(new CheckInvocation<DexNewExpr>() {
            });
        }

        private void add(Handler<?> handler) {
            put((Class<? extends DexElement>) JavaSuperTypeArgs.$(handler.getClass())[0], handler);
        }
    };

    private final TypeSystem ts;
    private boolean hasError;
    private TypeTable localTypeTable;

    public CheckSemanticError(TypeSystem ts, DexFile file) {
        this.ts = ts;
        visit(file);
    }

    public void localTypeTable(TypeTable localTypeTable) {
        this.localTypeTable = localTypeTable;
    }

    public TypeTable localTypeTable() {
        return localTypeTable;
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
        System.out.println("found semantic error: " + occuredAt);
        System.out.println(error);
        hasError = true;
    }

    public TypeSystem typeSystem() {
        return ts;
    }
}
