package com.dexscript.resolve;

import com.dexscript.ast.DexFile;
import com.dexscript.ast.DexFunction;
import com.dexscript.ast.DexInterface;
import com.dexscript.ast.DexRootDecl;
import com.dexscript.ast.expr.*;
import com.dexscript.ast.inf.DexInfFunction;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Resolve {

    private final ResolveType resolveType = new ResolveType();
    private final ResolveValue resolveValue = new ResolveValue();
    private final ResolveFunction resolveFunction = new ResolveFunction();

    public Resolve() {
        resolveValue.setResolve(this);
        resolveFunction.setResolve(this);
        resolveType.setResolve(this);
    }

    public void declare(DexFile file) {
        for (DexRootDecl rootDecl : file.rootDecls()) {
            if (rootDecl.function() != null) {
                declare(rootDecl.function());
            } else if (rootDecl.inf() != null) {
                declare(rootDecl.inf());
            }
        }
    }

    public void declare(DexFunction function) {
        resolveFunction.declare(function);
        resolveType.declare(function);
    }


    public void declare(DexInterface inf) {
        Denotation.InterfaceType infType = new Denotation.InterfaceType(this, inf);
        resolveType.declare(infType);
        for (Denotation.FunctionType functionType : infType.members()) {
            resolveFunction.declare(functionType);
        }
    }

    public void declare(Denotation.FunctionType functionType) {
        resolveFunction.declare(functionType);
    }

    @NotNull
    public List<Denotation.FunctionType> resolveFunctions(DexFunctionCallExpr callExpr) {
        return resolveFunction.resolveFunctions(callExpr);
    }

    @NotNull
    public List<Denotation.FunctionType> resolveFunctions(DexNewExpr newExpr) {
        return resolveFunction.resolveFunctions(newExpr);
    }

    @NotNull
    public List<Denotation.FunctionType> resolveFunctions(DexMethodCallExpr callExpr) {
        return resolveFunction.resolveFunctions(callExpr);
    }

    public List<Denotation.FunctionType> resolveFunctions(DexInfFunction infFunction) {
        return resolveFunction.resolveFunctions(infFunction);
    }

    @NotNull
    public Denotation resolveValue(DexReference ref) {
        return resolveValue.resolveValue(ref);
    }

    @NotNull
    public Denotation.Type resolveType(DexExpr expr) {
        return resolveFunction.resolveType(expr);
    }

    @NotNull
    public Denotation.Type resolveType(String name) {
        return resolveType(new DexReference(name));
    }

    @NotNull
    public Denotation.Type resolveType(DexReference ref) {
        return resolveType.resolveType(ref);
    }

    @NotNull
    public Denotation.Type resolveType(DexFunction function) {
        return resolveFunction.resolveType(function);
    }

    public boolean canProvide(String functionName, List<Denotation.Type> params, Denotation.Type ret) {
        return resolveFunction.canProvide(functionName, params, ret);
    }
}
