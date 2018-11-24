package com.dexscript.resolve;

import com.dexscript.ast.DexFunction;
import com.dexscript.ast.DexParam;
import com.dexscript.ast.core.DexElement;
import com.dexscript.ast.expr.DexReference;
import com.dexscript.ast.stmt.DexShortVarDecl;

import java.util.ArrayList;
import java.util.List;

final class ResolveValue {

    public static final DenotationTable BUILTIN_VALUES = new DenotationTable();
    private final DenotationTable builtin;
    private ResolveType resolveType;

    ResolveValue(DenotationTable builtin) {
        this.builtin = builtin;
    }

    ResolveValue() {
        this(BUILTIN_VALUES);
    }

    void setResolveType(ResolveType resolveType) {
        this.resolveType = resolveType;
    }

    public Denotation.Value __(DexReference ref) {
        List<DexElement> prevElems = new ArrayList<>();
        DexElement current = ref;
        while (true) {
            current = current.prev();
            if (current == null) {
                break;
            }
            prevElems.add(current);
        }
        DenotationTable parentDT = builtin;
        for (int i = prevElems.size() - 1; i >= 0; i--) {
            DexElement elem = prevElems.get(i);
            parentDT = denotationTable(elem, parentDT);
        }
        return (Denotation.Value) parentDT.get(ref.toString());
    }

    private DenotationTable denotationTable(DexElement elem, DenotationTable parentDT) {
        DenotationTable denotationTable = elem.attachmentOfType(DenotationTable.class);
        if (denotationTable != null) {
            return denotationTable;
        }
        denotationTable = elem.attach(parentDT.copy());
        if (elem instanceof DexFunction) {
            return fillTable((DexFunction) elem, denotationTable);
        }
        if (elem instanceof DexShortVarDecl) {
            return fillTable((DexShortVarDecl)elem, denotationTable);
        }
        return parentDT;
    }

    private DenotationTable fillTable(DexFunction function, DenotationTable denotationTable) {
        for (DexParam param : function.sig().params()) {
            String name = param.paramName().toString();
            Denotation.Type type = resolveType.__(param.paramType());
            if (type != null) {
                denotationTable.put(name, new Denotation.Value(name, type, param));
            }
        }
        return denotationTable;
    }

    private DenotationTable fillTable(DexShortVarDecl shortVarDecl, DenotationTable denotationTable) {
        String name = shortVarDecl.decls().get(0).toString();
        Denotation.Type type = resolveType.__(shortVarDecl.expr());
        if (type != null) {
            denotationTable.put(name, new Denotation.Value(name, type, shortVarDecl));
        }
        return denotationTable;
    }
}
