package com.dexscript.resolve;

import com.dexscript.ast.DexFunction;
import com.dexscript.ast.DexParam;
import com.dexscript.ast.core.DexElement;
import com.dexscript.ast.expr.DexReference;

import java.util.ArrayList;
import java.util.List;

public final class ResolveValue {

    public static final DenotationTable BUILTIN_VALUES = new DenotationTable();
    private final DenotationTable builtin;
    private final ResolveType resolveType;

    public ResolveValue(DenotationTable builtin, ResolveType resolveType) {
        this.builtin = builtin;
        this.resolveType = resolveType;
    }

    public ResolveValue() {
        this(BUILTIN_VALUES, new ResolveType());
    }

    public Denotation.Value __(DexReference ref) {
        List<DexElement> prevElems = new ArrayList<>();
        DexElement current = ref;
        do {
            current = current.prev();
            prevElems.add(current);
        } while (current != null);
        DenotationTable parentDT = builtin;
        for (int i = prevElems.size() - 1; i >= 0; i--) {
            DexElement elem = prevElems.get(i);
            parentDT = denotationTable(elem, parentDT);
        }
        return (Denotation.Value) parentDT.get(ref.toString());
    }

    private DenotationTable denotationTable(DexElement elem, DenotationTable parentDT) {
        if (elem instanceof DexFunction) {
            return denotationTable((DexFunction) elem, parentDT);
        }
        return parentDT;
    }

    private DenotationTable denotationTable(DexFunction function, DenotationTable parentDT) {
        DenotationTable denotationTable = function.attachmentOfType(DenotationTable.class);
        if (denotationTable != null) {
            return denotationTable;
        }
        denotationTable = function.attach(parentDT.copy());
        for (DexParam param : function.sig().params()) {
            String name = param.paramName().toString();
            Denotation.Type type = resolveType.__(param.paramType());
            if (type != null) {
                denotationTable.put(name, new Denotation.Value(name, type, param));
            }
        }
        return denotationTable;
    }
}
