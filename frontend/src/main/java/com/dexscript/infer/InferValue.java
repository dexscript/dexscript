package com.dexscript.infer;

import com.dexscript.ast.DexActorBody;
import com.dexscript.ast.DexFile;
import com.dexscript.ast.core.DexElement;
import com.dexscript.ast.expr.DexLessThanExpr;
import com.dexscript.ast.expr.DexValueRef;
import com.dexscript.ast.stmt.*;
import com.dexscript.type.core.DType;
import com.dexscript.type.core.JavaSuperTypeArgs;
import com.dexscript.type.core.TypeSystem;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface InferValue<E extends DexElement> {

    interface OnUnknownElem {
        void handle(DexElement elem);
    }

    class Events {
        public static OnUnknownElem ON_UNKNOWN_ELEM = elem -> {
            throw new UnsupportedOperationException("not implemented: " + elem.getClass() + "\n" + elem);
        };
    }

    Map<Class<? extends DexElement>, InferValue> handlers = new HashMap<Class<? extends DexElement>, InferValue>() {
        {
            put(DexActorBody.class, (ts, elem, table) -> {
            });
            put(DexBlock.class, (ts, elem, table) -> {
            });
            put(DexReturnStmt.class, (ts, elem, table) -> {
            });
            put(DexAwaitStmt.class, (ts, elem, table) -> {
            });
            put(DexProduceStmt.class, (ts, elem, table) -> {
            });
            put(DexFile.class, (ts, elem, table) -> {
            });
            put(DexExprStmt.class, (ts, elem, table) -> {
            });
            put(DexAssignStmt.class, (ts, elem, table) -> {
            });
            put(DexIfStmt.class, (ts, elem, table) -> {
            });
            put(DexElseStmt.class, (ts, elem, table) -> {
            });
            put(DexIncrStmt.class, (ts, elem, table) -> {
            });
            put(DexLessThanExpr.class, (ts, elem, table) -> {
            });
            add(new InferVarDecl());
            add(new InferShortVarDecl());
            add(new InferActor());
            add(new InferAwaitConsumer());
            add(new InferForStmt());
        }

        private void add(InferValue<?> handler) {
            put((Class<? extends DexElement>) JavaSuperTypeArgs.$(handler.getClass())[0], handler);
        }
    };

    void handle(TypeSystem ts, E elem, ValueTable table);

    static Value $(TypeSystem ts, DexValueRef ref) {
        String refName = ref.toString();
        if (refName.equals("$")) {
            DType contextType = ts.context(ref.pkg());
            return new Value("$", contextType, null);
        }
        if (ref.isGlobalScope()) {
            return new Value(ref.toString(), InferType.$(ts, ref), null);
        }
        List<DexElement> prevElems = collectPrevElems(ref);
        ValueTable parentTable = new ValueTable();
        for (int i = prevElems.size() - 1; i >= 0; i--) {
            DexElement elem = prevElems.get(i);
            parentTable = loadTable(ts, elem, parentTable);
        }
        return parentTable.resolveValue(refName);
    }

    @NotNull
    static List<DexElement> collectPrevElems(DexValueRef ref) {
        List<DexElement> prevElems = new ArrayList<>();
        DexElement current = ref;
        while (true) {
            current = current.prev();
            if (current == null) {
                break;
            }
            prevElems.add(current);
        }
        return prevElems;
    }

    static ValueTable loadTable(TypeSystem ts, DexElement elem, ValueTable parentTable) {
        ValueTable table = elem.attachmentOfType(ValueTable.class);
        if (table != null) {
            return table;
        }
        InferValue inferValue = handlers.get(elem.getClass());
        if (inferValue == null) {
            Events.ON_UNKNOWN_ELEM.handle(elem);
            return parentTable;
        }
        table = elem.attach(parentTable.copy());
        inferValue.handle(ts, elem, table);
        return table;
    }
}
