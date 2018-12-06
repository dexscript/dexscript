package com.dexscript.infer;

import com.dexscript.ast.DexFunctionBody;
import com.dexscript.ast.core.DexElement;
import com.dexscript.ast.expr.DexValueRef;
import com.dexscript.ast.func.DexBlock;
import com.dexscript.ast.func.DexReturnStmt;
import com.dexscript.type.TypeSystem;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface InferValue<E extends DexElement> {

    interface OnUnknownElem {
        void handle(Class<? extends DexElement> clazz);
    }

    class Events {
        public static OnUnknownElem ON_UNKNOWN_ELEM = clazz -> {
        };
    }

    Map<Class<? extends DexElement>, InferValue> handlers = new HashMap<>() {
        {
            put(DexFunctionBody.class, (ts, elem, table) -> {
            });
            put(DexBlock.class, (ts, elem, table) -> {
            });
            put(DexReturnStmt.class, (ts, elem, table) -> {
            });
            add(new InferShortVarDecl());
            add(new InferFunction());
        }

        private void add(InferValue<?> handler) {
            ParameterizedType translateInf = (ParameterizedType) handler.getClass().getGenericInterfaces()[0];
            put((Class<? extends DexElement>) translateInf.getActualTypeArguments()[0], handler);
        }
    };

    void handle(TypeSystem ts, E elem, ValueTable table);

    static Value $(TypeSystem ts, DexValueRef ref) {
        List<DexElement> prevElems = new ArrayList<>();
        DexElement current = ref;
        while (true) {
            current = current.prev();
            if (current == null) {
                break;
            }
            prevElems.add(current);
        }
        ValueTable parentTable = new ValueTable();
        for (int i = prevElems.size() - 1; i >= 0; i--) {
            DexElement elem = prevElems.get(i);
            parentTable = loadTable(ts, elem, parentTable);
        }
        return parentTable.resolveValue(ref.toString());
    }

    private static ValueTable loadTable(TypeSystem ts, DexElement elem, ValueTable parentTable) {
        ValueTable table = elem.attachmentOfType(ValueTable.class);
        if (table != null) {
            return table;
        }
        InferValue inferValue = handlers.get(elem.getClass());
        if (inferValue == null) {
            Events.ON_UNKNOWN_ELEM.handle(elem.getClass());
            return parentTable;
        }
        table = elem.attach(parentTable.copy());
        inferValue.handle(ts, elem, table);
        return table;
    }
}
