package com.dexscript.resolve;

import com.dexscript.ast.DexFile;
import com.dexscript.ast.DexFunction;
import com.dexscript.ast.DexParam;
import com.dexscript.ast.DexRootDecl;
import com.dexscript.ast.expr.DexReference;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

final class ResolveFunction {

    private final Map<String, List<Denotation.Type>> defined = new HashMap<>();
    private ResolveType resolveType;

    void setResolveType(ResolveType resolveType) {
        this.resolveType = resolveType;
    }

    public void define(DexFile file) {
        for (DexRootDecl rootDecl : file.rootDecls()) {
            if (rootDecl instanceof DexFunction) {
                define((DexFunction)rootDecl);
            }
        }
    }

    public void define(DexFunction function) {
        String functionName = function.identifier().toString();
        List<Denotation.Type> types = defined.computeIfAbsent(functionName, k -> new ArrayList<>());
        List<Denotation.Type> args = new ArrayList<>();
        for (DexParam param : function.sig().params()) {
            Denotation.Type arg = (Denotation.Type) resolveType.__(param.paramType());
            args.add(arg);
        }
        Denotation.Type ret = (Denotation.Type) resolveType.__(function.sig().ret());
        types.add(Denotation.function(functionName, function, args, ret));
    }

    @NotNull
    public Denotation __(DexReference ref) {
        String refName = ref.toString();
        List<Denotation.Type> types = defined.get(refName);
        if (types == null) {
            return new Denotation.Error(refName, ref, "can not resolve " + refName + " to a function");
        }
        return types.get(0);
    }
}
