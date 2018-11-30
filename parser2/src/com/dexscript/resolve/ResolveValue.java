package com.dexscript.resolve;

import com.dexscript.ast.DexFunction;
import com.dexscript.ast.DexParam;
import com.dexscript.ast.core.DexElement;
import com.dexscript.ast.expr.DexReference;
import com.dexscript.ast.func.DexShortVarDecl;

import java.util.ArrayList;
import java.util.List;

final class ResolveValue {

    public static final DenotationTable<Denotation.Value> BUILTIN_VALUES = new DenotationTable<>();
    private final DenotationTable<Denotation.Value> builtin;
    private Resolve resolve;

    ResolveValue(DenotationTable<Denotation.Value> builtin) {
        this.builtin = builtin;
    }

    ResolveValue() {
        this(BUILTIN_VALUES);
    }

    void setResolve(Resolve resolveType) {
        this.resolve = resolveType;
    }

    public Denotation resolveValue(DexReference ref) {
        Denotation denotation = ref.attachmentOfType(Denotation.class);
        if (denotation != null) {
            return denotation;
        }
        denotation = _resolveValue(ref);
        ref.attach(denotation);
        return denotation;
    }

    private Denotation _resolveValue(DexReference ref) {
        List<DexElement> prevElems = new ArrayList<>();
        DexElement current = ref;
        while (true) {
            current = current.prev();
            if (current == null) {
                break;
            }
            prevElems.add(current);
        }
        DenotationTable<Denotation.Value> parentDT = builtin;
        for (int i = prevElems.size() - 1; i >= 0; i--) {
            DexElement elem = prevElems.get(i);
            parentDT = denotationTable(elem, parentDT);
        }
        String refName = ref.toString();
        Denotation denotation = parentDT.get(refName);
        if (denotation == null) {
            return new Denotation.Error(refName, ref, "can not resolve " + refName + " to a value");
        }
        return denotation;
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
            return fillTable((DexShortVarDecl) elem, denotationTable);
        }
        return parentDT;
    }

    private DenotationTable fillTable(DexFunction function, DenotationTable denotationTable) {
        for (DexParam param : function.sig().params()) {
            String name = param.paramName().toString();
            Denotation.Type type = (Denotation.Type) resolve.resolveType(param.paramType());
            if (type != null) {
                denotationTable.put(name, new Denotation.Value(name, type, param));
            }
        }
        return denotationTable;
    }

    private DenotationTable fillTable(DexShortVarDecl shortVarDecl, DenotationTable denotationTable) {
        String name = shortVarDecl.decls().get(0).toString();
        Denotation.Type type = resolve.resolveType(shortVarDecl.expr());
        if (type != null) {
            denotationTable.put(name, new Denotation.Value(name, type, shortVarDecl));
        }
        return denotationTable;
    }
}
