package com.dexscript.resolve;

import com.dexscript.ast.DexFunction;
import com.dexscript.ast.expr.DexReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

final class ResolveType {

    private final Map<String, List<DexFunction>> declaredFunctions = new HashMap<>();
    private final DenotationTable<Denotation.Type> declared;
    private Resolve resolve;

    ResolveType(DenotationTable<Denotation.Type> builtin) {
        this.declared = builtin;
    }

    ResolveType() {
        this(BuiltinTypes.BUILTIN_TYPES);
    }

    public void setResolve(Resolve resolve) {
        this.resolve = resolve;
    }

    public void declare(Denotation.InterfaceType inf) {
        declared.put(inf.name(), inf);
    }

    public Denotation.Type resolveType(DexReference ref) {
        Denotation.Type type = ref.attachmentOfType(Denotation.Type.class);
        if (type != null) {
            return type;
        }
        String refName = ref.toString();
        type = declared.get(refName);
        if (type != null) {
            return type;
        }
        List<DexFunction> functions = declaredFunctions.get(refName);
        if (functions != null) {
            for (DexFunction function : functions) {
                return resolve.resolveType(function);
            }
        }
        type = BuiltinTypes.UNDEFINED_TYPE;
        ref.attach(type);
        return type;
    }

    public void declare(DexFunction function) {
        String functionName = function.identifier().toString();
        List<DexFunction> functions = declaredFunctions.computeIfAbsent(functionName, k -> new ArrayList<>());
        functions.add(function);
    }
}
