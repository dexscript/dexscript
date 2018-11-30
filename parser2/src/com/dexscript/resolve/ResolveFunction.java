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
    private Resolve resolve;

    void setResolve(Resolve resolve) {
        this.resolve = resolve;
    }

    public void declare(DexFunction function) {
        String functionName = function.identifier().toString();
        List<Denotation.FunctionType> types = defined.computeIfAbsent(functionName, k -> new ArrayList<>());
        List<Denotation.Type> args = new ArrayList<>();
        for (DexParam param : function.sig().params()) {
            args.add(resolve.resolveType(param.paramType()));
        }
        Denotation.Type ret = resolve.resolveType(function.sig().ret());
        types.add(new Denotation.FunctionType(functionName, function, args, ret));
    }

    @NotNull
    public Denotation resolveFunction(DexNewExpr newExpr) {
        DexReference ref = newExpr.target().asRef();
        String functionName = ref.toString();
        List<Denotation.Type> argTypes = new ArrayList<>();
        for (DexExpr arg : newExpr.args()) {
            argTypes.add(resolve.resolveType(arg));
        }
        return resolveFunction(newExpr, functionName, argTypes);
    }

    @NotNull
    public Denotation resolveFunction(DexFunctionCallExpr callExpr) {
        DexReference ref = callExpr.target().asRef();
        String functionName = ref.toString();
        List<Denotation.Type> argTypes = new ArrayList<>();
        for (DexExpr arg : callExpr.args()) {
            argTypes.add(resolve.resolveType(arg));
        }
        return resolveFunction(callExpr, functionName, argTypes);
    }

    @NotNull
    public Denotation resolveFunction(DexMethodCallExpr callExpr) {
        DexReference ref = callExpr.method();
        String functionName = ref.toString();
        List<Denotation.Type> argTypes = new ArrayList<>();
        argTypes.add(resolve.resolveType(callExpr.obj()));
        for (DexExpr arg : callExpr.args()) {
            argTypes.add(resolve.resolveType(arg));
        }
        return resolveFunction(callExpr, functionName, argTypes);
    }

    @NotNull
    public Denotation resolveFunction(DexAddExpr addExpr) {
        List<Denotation.Type> argTypes = new ArrayList<>();
        argTypes.add(resolve.resolveType(addExpr.left()));
        argTypes.add(resolve.resolveType(addExpr.right()));
        return resolveFunction(addExpr, "Add__", argTypes);
    }

    @NotNull
    public Denotation resolveFunction(DexGetResultExpr getResultExpr) {
        List<Denotation.Type> argTypes = new ArrayList<>();
        argTypes.add(resolve.resolveType(getResultExpr.right()));
        return resolveFunction(getResultExpr, "GetResult__", argTypes);
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
            Denotation.Type paramType = (Denotation.Type) resolve.resolveType(param.paramType());
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

    public Denotation.Type resolveType(DexExpr expr) {
        Denotation.Type denotation = expr.attachmentOfType(Denotation.Type.class);
        if (denotation != null) {
            return denotation;
        }
        denotation = _resolveType(expr);
        expr.attach(denotation);
        return denotation;
    }

    private Denotation.Type _resolveType(DexExpr expr) {
        if (expr instanceof DexIntegerLiteral) {
            return BuiltinTypes.INT64_TYPE;
        }
        if (expr instanceof DexFloatLiteral) {
            return BuiltinTypes.FLOAT64_TYPE;
        }
        if (expr instanceof DexStringLiteral) {
            return BuiltinTypes.STRING_TYPE;
        }
        if (expr instanceof DexReference) {
            return eval((DexReference)expr);
        }
        if (expr instanceof DexParenExpr) {
            return resolveType(((DexParenExpr)expr).body());
        }
        if (expr instanceof DexFunctionCallExpr) {
            return eval((DexFunctionCallExpr)expr);
        }
        if (expr instanceof DexMethodCallExpr) {
            return eval((DexMethodCallExpr)expr);
        }
        if (expr instanceof DexAddExpr) {
            return eval((DexAddExpr)expr);
        }
        if (expr instanceof DexNewExpr) {
            return eval((DexNewExpr)expr);
        }
        if (expr instanceof DexGetResultExpr) {
            return eval((DexGetResultExpr)expr);
        }
        return BuiltinTypes.UNDEFINED_TYPE;
    }

    private Denotation.Type eval(DexReference expr) {
        Denotation refObj = resolve.resolveValue(expr);
        if (refObj instanceof Denotation.Value) {
            Denotation.Value ref = (Denotation.Value) refObj;
            return (Denotation.Type) ref.type();
        }
        return BuiltinTypes.UNDEFINED_TYPE;
    }

    private Denotation.Type eval(DexGetResultExpr expr) {
        Denotation typeObj = resolveFunction(expr);
        if (typeObj instanceof Denotation.FunctionType) {
            return ((Denotation.FunctionType)typeObj).ret();
        }
        return BuiltinTypes.UNDEFINED_TYPE;
    }

    private Denotation.Type eval(DexFunctionCallExpr functionCallExpr) {
        Denotation typeObj = resolveFunction(functionCallExpr);
        if (typeObj instanceof Denotation.FunctionType) {
            return ((Denotation.FunctionType) typeObj).ret();
        }
        return BuiltinTypes.UNDEFINED_TYPE;
    }

    private Denotation.Type eval(DexMethodCallExpr methodCallExpr) {
        Denotation typeObj = resolveFunction(methodCallExpr);
        if (typeObj instanceof Denotation.FunctionType) {
            return ((Denotation.FunctionType) typeObj).ret();
        }
        return BuiltinTypes.UNDEFINED_TYPE;
    }

    private Denotation.Type eval(DexAddExpr addExpr) {
        Denotation typeObj = resolveFunction(addExpr);
        if (typeObj instanceof Denotation.FunctionType) {
            return ((Denotation.FunctionType) typeObj).ret();
        }
        return BuiltinTypes.UNDEFINED_TYPE;
    }

    private Denotation.Type eval(DexNewExpr newExpr) {
        Denotation typeObj = resolveFunction(newExpr);
        if (typeObj instanceof Denotation.FunctionType) {
            DexFunction definedBy = (DexFunction) ((Denotation.FunctionType) typeObj).definedBy();
            return new Denotation.FunctionInterfaceType(resolve, definedBy);
        }
        return BuiltinTypes.UNDEFINED_TYPE;
    }
}
