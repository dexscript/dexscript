package com.dexscript.resolve;

import com.dexscript.ast.DexFile;
import com.dexscript.ast.DexFunction;
import com.dexscript.ast.DexRootDecl;
import com.dexscript.ast.expr.DexExpr;
import com.dexscript.ast.expr.DexReference;
import org.jetbrains.annotations.NotNull;

public class Resolve {

    private final ResolveType resolveType = new ResolveType();
    private final ResolveValue resolveValue = new ResolveValue();
    private final ResolveFunction resolveFunction = new ResolveFunction();

    public Resolve() {
        resolveValue.setResolveType(resolveType);
        resolveFunction.setResolveType(resolveType);
        resolveType.setResolveFunction(resolveFunction);
    }

    public void define(DexFile file) {
        for (DexRootDecl rootDecl : file.rootDecls()) {
            if (rootDecl instanceof DexFunction) {
                define((DexFunction) rootDecl);
            }
        }
    }

    public void define(DexFunction function) {
        resolveFunction.define(function);
    }

    @NotNull
    public Denotation resolveFunction(DexReference ref) {
        return resolveFunction.__(ref);
    }

    @NotNull
    public Denotation resolveType(DexReference ref) {
        return resolveType.__(ref);
    }

    @NotNull
    public Denotation resolveValue(DexReference ref) {
        return resolveValue.__(ref);
    }

    @NotNull
    public Denotation resolveType(DexExpr expr) {
        return resolveType.__(expr);
    }
}
