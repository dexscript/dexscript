package com.dexscript.resolve;

import com.dexscript.ast.DexFile;
import com.dexscript.ast.DexFunction;
import com.dexscript.ast.DexInterface;
import com.dexscript.ast.expr.DexFunctionCallExpr;
import com.dexscript.ast.expr.DexExpr;
import com.dexscript.ast.expr.DexReference;
import org.jetbrains.annotations.NotNull;

import java.util.List;

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

    public void declare(DexFile file) {
        resolveFunction.declare(file);
    }

    public void declare(DexFunction function) {
        resolveFunction.declare(function);
    }


    public void declare(DexInterface inf) {
        resolveType.declare(new Denotation.InterfaceType(this, inf));
    }

    @NotNull
    public Denotation resolveFunction(DexFunctionCallExpr callExpr) {
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

    public boolean canProvide(String functionName, List<Denotation.Type> params, Denotation.Type ret) {
        return resolveFunction.canProvide(functionName, params, ret);
    }
}
