package com.dexscript.pkg;

import com.dexscript.ast.DexFile;
import com.dexscript.ast.core.DexElement;
import com.dexscript.ast.expr.*;
import com.dexscript.type.core.JavaSuperTypeArgs;
import com.dexscript.type.core.TypeSystem;
import com.dexscript.type.core.TypeTable;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

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
            add(new CheckInfMethod());
            add(new CheckInfFunction());
            add(new CheckInvocation<DexFunctionCallExpr>() {
            });
            add(new CheckInvocation<DexMethodCallExpr>() {
            });
            add(new CheckInvocation<DexNewExpr>() {
            });
            add(new CheckInvocation<DexEqualExpr>() {
            });
            add(new CheckInvocation<DexIndexExpr>() {
            });
            add(new CheckInvocation<DexFieldExpr>() {
            });
            add(new CheckInvocation<DexAddExpr>() {
            });
            add(new CheckInvocation<DexLessThanExpr>() {
            });
            add(new CheckInvocation<DexGreaterThanExpr>() {
            });
        }

        private void add(Handler<?> handler) {
            put((Class<? extends DexElement>) JavaSuperTypeArgs.$(handler.getClass())[0], handler);
        }
    };

    private final TypeSystem ts;
    private boolean hasError;
    private Stack<TypeTable> localTypeTables = new Stack<>();

    public CheckSemanticError(TypeSystem ts, DexFile file) {
        this.ts = ts;
        visit(file);
    }

    public interface Action {
        void apply();
    }

    public void withTypeTable(TypeTable localTypeTable, Action action) {
        localTypeTables.push(localTypeTable);
        action.apply();
        localTypeTables.pop();
    }

    public TypeTable localTypeTable() {
        if (localTypeTables.isEmpty()) {
            return null;
        }
        return localTypeTables.peek();
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
