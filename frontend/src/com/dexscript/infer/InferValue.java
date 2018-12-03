package com.dexscript.infer;

import com.dexscript.ast.DexFunction;
import com.dexscript.ast.DexFunctionBody;
import com.dexscript.ast.core.DexElement;
import com.dexscript.ast.expr.DexReference;
import com.dexscript.ast.func.DexBlock;
import com.dexscript.ast.func.DexReturnStmt;
import com.dexscript.type.TypeSystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface InferValue {

    Map<Class<? extends DexElement>, InferValue> inferValues = new HashMap<>() {{
        put(DexFunction.class, new InferFunction());
        put(DexFunctionBody.class, (ts, elem, table) -> {
        });
        put(DexBlock.class, (ts, elem, table) -> {
        });
        put(DexReturnStmt.class, (ts, elem, table) -> {
        });
    }};

    void fillTable(TypeSystem ts, DexElement elem, ValueTable table);

    static Value inferValue(TypeSystem ts, DexReference ref) {
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
        table = elem.attach(parentTable.copy());
        InferValue inferValue = inferValues.get(elem.getClass());
        if (inferValue == null) {
            throw new UnsupportedOperationException("not implemented: " + elem.getClass());
        }
        inferValue.fillTable(ts, elem, table);
        return table;
    }
}
