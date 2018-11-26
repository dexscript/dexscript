package com.dexscript.resolve;

import com.dexscript.ast.DexFunction;
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
    }

    public void define(DexFunction function) {
        resolveFunction.define(function);
    }

    public Denotation.Type resolveFunction(DexReference ref) {
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

    public Denotation.Type resolveType(DexExpr expr) {
        return resolveType.__(expr);
    }
}
