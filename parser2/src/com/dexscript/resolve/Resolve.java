package com.dexscript.resolve;

import com.dexscript.ast.DexFile;
import com.dexscript.ast.DexFunction;
import com.dexscript.ast.DexInterface;
import com.dexscript.ast.expr.DexCallExpr;
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
        resolveType.setResolveValue(resolveValue);
    }

    public void define(DexFile file) {
        resolveFunction.define(file);
    }

    public void define(DexFunction function) {
        resolveFunction.define(function);
    }


    public void define(DexInterface inf) {
        resolveType.define(new Denotation.InterfaceType(this, inf));
    }

    @NotNull
    public Denotation resolveFunction(DexCallExpr callExpr) {
        return resolveFunction.resolveFunction(callExpr);
    }

    @NotNull
    public Denotation resolveType(DexReference ref) {
        return resolveType.resolveType(ref);
    }

    @NotNull
    public Denotation resolveValue(DexReference ref) {
        return resolveValue.resolveValue(ref);
    }

    @NotNull
    public Denotation resolveType(DexExpr expr) {
        return resolveType.resolveType(expr);
    }

    public Denotation resolveType(String name) {
        return resolveType(new DexReference(name));
    }
}
