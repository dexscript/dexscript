package com.dexscript.resolve;

import com.dexscript.ast.DexFunction;
import com.dexscript.ast.expr.DexReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

final class ResolveType {

    private final Map<String, List<DexFunction>> definedFunctions = new HashMap<>();
    private final DenotationTable<Denotation.Type> defined = BuiltinTypes.BUILTIN_TYPES;
    private final Resolve resolve;

    ResolveType(Resolve resolve) {
        this.resolve = resolve;
    }

    public void define(Denotation.InterfaceType inf) {
        defined.put(inf.name(), inf);
    }

    public Denotation.Type resolveType(DexReference ref) {
        Denotation.Type type = ref.attachmentOfType(Denotation.Type.class);
        if (type != null) {
            return type;
        }
        String refName = ref.toString();
        type = defined.get(refName);
        if (type != null) {
            return type;
        }
        List<DexFunction> functions = definedFunctions.get(refName);
        if (functions != null) {
            for (DexFunction function : functions) {
                return resolve.resolveType(function);
            }
        }
        type = BuiltinTypes.UNDEFINED_TYPE;
        ref.attach(type);
        return type;
    }

    public void define(DexFunction function) {
        String functionName = function.identifier().toString();
        List<DexFunction> functions = definedFunctions.computeIfAbsent(functionName, k -> new ArrayList<>());
        functions.add(function);
    }
}
