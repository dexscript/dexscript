package com.dexscript.resolve;

import com.dexscript.ast.DexFile;
import com.dexscript.ast.DexFunction;
import com.dexscript.ast.DexParam;
import com.dexscript.ast.DexRootDecl;
import com.dexscript.ast.expr.DexReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResolveFunction {

    private final Map<String, List<Denotation.Type>> defined = new HashMap<>();
    private final ResolveType resolveType;

    public ResolveFunction(ResolveType resolveType) {
        this.resolveType = resolveType;
    }

    public ResolveFunction() {
        this.resolveType = new ResolveType();
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
            Denotation.Type arg = resolveType.__(param.paramType());
            args.add(arg);
        }
        Denotation.Type ret = resolveType.__(function.sig().ret());
        types.add(Denotation.function(functionName, function, args, ret));
    }

    public Denotation.Type __(DexReference ref) {
        List<Denotation.Type> types = defined.get(ref.toString());
        if (types == null) {
            return null;
        }
        return types.get(0);
    }
}
