package com.dexscript.resolve;

import com.dexscript.ast.*;
import com.dexscript.ast.core.DexElement;
import com.dexscript.ast.expr.*;
import com.dexscript.ast.inf.DexInfFunction;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

final class ResolveFunction {

    private final Map<String, List<Denotation.FunctionType>> defined = new HashMap<>();
    private ResolveType resolveType;

    void setResolveType(ResolveType resolveType) {
        this.resolveType = resolveType;
    }

    public void declare(DexFunction function) {
        String functionName = function.identifier().toString();
        List<Denotation.FunctionType> types = defined.computeIfAbsent(functionName, k -> new ArrayList<>());
        List<Denotation.Type> args = new ArrayList<>();
        for (DexParam param : function.sig().params()) {
            Denotation typeObj = resolveType.resolveType(param.paramType());
            if (!(typeObj instanceof Denotation.Type)) {
                return;
            }
            Denotation.Type arg = (Denotation.Type) typeObj;
            args.add(arg);
        }
        Denotation.Type ret = (Denotation.Type) resolveType.resolveType(function.sig().ret());
        types.add(new Denotation.FunctionType(functionName, function, args, ret));
    }

    @NotNull
    public Denotation resolveFunction(DexFunctionCallExpr callExpr) {
        DexReference ref = callExpr.target().asRef();
        String functionName = ref.toString();
        List<Denotation.Type> argTypes = new ArrayList<>();
        for (DexExpr arg : callExpr.args()) {
            argTypes.add((Denotation.Type) resolveType.resolveType(arg));
        }
        return resolveFunction(callExpr, functionName, argTypes);
    }

    @NotNull
    public Denotation resolveFunction(DexMethodCallExpr callExpr) {
        DexReference ref = callExpr.method();
        String functionName = ref.toString();
        List<Denotation.Type> argTypes = new ArrayList<>();
        argTypes.add((Denotation.Type) resolveType.resolveType(callExpr.obj()));
        for (DexExpr arg : callExpr.args()) {
            argTypes.add((Denotation.Type) resolveType.resolveType(arg));
        }
        return resolveFunction(callExpr, functionName, argTypes);
    }

    @NotNull
    public Denotation resolveFunction(DexAddExpr addExpr) {
        List<Denotation.Type> argTypes = new ArrayList<>();
        argTypes.add((Denotation.Type) resolveType.resolveType(addExpr.left()));
        argTypes.add((Denotation.Type) resolveType.resolveType(addExpr.right()));
        return resolveFunction(addExpr, "Add__", argTypes);
    }

    private Denotation resolveFunction(DexElement elem, String functionName, List<Denotation.Type> paramTypes) {
        List<Denotation.FunctionType> candidates = defined.get(functionName);
        if (candidates == null) {
            return new Denotation.Error(functionName, elem, "can not resolve " + functionName + " to a function");
        }
        for (Denotation.FunctionType candidate : candidates) {
            if (signatureMatch(paramTypes, candidate.params())) {
                return candidate;
            }
        }
        return new Denotation.Error(functionName, elem, "can not resolve " + functionName + " to a function");
    }

    private boolean signatureMatch(List<Denotation.Type> argTypes, List<Denotation.Type> paramTypes) {
        if (paramTypes.size() != argTypes.size()) {
            return false;
        }
        for (int i = 0; i < paramTypes.size(); i++) {
            Denotation.Type arg = paramTypes.get(i);
            if (!arg.isAssignableFrom(argTypes.get(i))) {
                return false;
            }
        }
        return true;
    }

    public boolean canProvide(String functionName, List<Denotation.Type> params, Denotation.Type ret) {
        List<Denotation.FunctionType> candidates = defined.get(functionName);
        if (candidates == null) {
            return false;
        }
        for (Denotation.FunctionType candidate : candidates) {
            if (candidate.canProvide(functionName, params, ret)) {
                return true;
            }
        }
        return false;
    }

    public void declare(Denotation.FunctionType functionType) {
        List<Denotation.FunctionType> functionTypes = defined.computeIfAbsent(
                functionType.name(), k -> new ArrayList<>());
        functionTypes.add(functionType);
    }

    public List<Denotation.FunctionType> resolveFunctions(DexInfFunction infFunction) {
        List<Denotation.FunctionType> candidates = defined.get(infFunction.identifier().toString());
        List<Denotation.Type> paramTypes = new ArrayList<>();
        for (DexParam param : infFunction.sig().params()) {
            Denotation.Type paramType = (Denotation.Type) resolveType.resolveType(param.paramType());
            paramTypes.add(paramType);
        }
        List<Denotation.FunctionType> impls = new ArrayList<>();
        for (Denotation.FunctionType candidate : candidates) {
            if (!candidate.isImpl()){
                continue;
            }
            if (signatureMatch(candidate.params(), paramTypes)) {
                impls.add(candidate);
            }
        }
        return impls;
    }
}
