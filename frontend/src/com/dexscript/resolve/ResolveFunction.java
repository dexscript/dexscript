package com.dexscript.resolve;

import com.dexscript.ast.*;
import com.dexscript.ast.expr.*;
import com.dexscript.ast.inf.DexInfFunction;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.dexscript.resolve.Denotation.FunctionType.returnTypeOf;

final class ResolveFunction {

    private final Map<String, List<Denotation.Function>> concreteDefs = new HashMap<>();
    private final Map<String, List<Denotation.FunctionType>> virtualDefs = new HashMap<>();
    private final Resolve resolve;

    public ResolveFunction(Resolve resolve) {
        this.resolve = resolve;
    }

    public void define(DexFunction elem) {
        String functionName = elem.identifier().toString();
        List<Denotation.Function> functions = concreteDefs.computeIfAbsent(functionName, k -> new ArrayList<>());
        List<Denotation.Type> args = new ArrayList<>();
        for (DexParam param : elem.sig().params()) {
            args.add(resolve.resolveType(param.paramType()));
        }
        Denotation.Type ret = resolve.resolveType(elem.sig().ret());
        Denotation.FunctionType functionType = new Denotation.FunctionType(functionName, elem, args, ret);
        Denotation.ActorType actorType = new Denotation.ActorType(resolve, elem);
        Denotation.Function function = new Denotation.Function(functionName, functionType, actorType, elem);
        functions.add(function);
    }

    public void define(Denotation.FunctionType functionType) {
        List<Denotation.FunctionType> functionTypes = virtualDefs.computeIfAbsent(
                functionType.name(), k -> new ArrayList<>());
        functionTypes.add(functionType);
    }

    @NotNull
    public List<Denotation.Function> resolveFunctions(DexNewExpr newExpr) {
        String functionName = newExpr.target().asRef().toString();
        List<Denotation.Function> candidates = concreteDefs.get(functionName);
        List<Denotation.Type> argTypes = new ArrayList<>();
        for (DexExpr arg : newExpr.args()) {
            Denotation.Type argType = resolveType(arg);
            argTypes.add(argType);
        }
        List<Denotation.Function> impls = new ArrayList<>();
        for (Denotation.Function candidate : candidates) {
            boolean paramsMatched = areParamsAssignable(candidate.functionType().params(), argTypes);
            if (paramsMatched) {
                impls.add(candidate);
            }
        }
        return impls;
    }

    @NotNull
    public List<Denotation.Function> resolveFunctions(DexInfFunction infFunction) {
        String functionName = infFunction.identifier().toString();
        List<Denotation.Function> candidates = concreteDefs.get(functionName);
        List<Denotation.Type> paramTypes = new ArrayList<>();
        for (DexParam param : infFunction.sig().params()) {
            Denotation.Type paramType = resolve.resolveType(param.paramType());
            paramTypes.add(paramType);
        }
        Denotation.Type retType = resolve.resolveType(infFunction.sig().ret());
        List<Denotation.Function> impls = new ArrayList<>();
        for (Denotation.Function candidate : candidates) {
            boolean paramsMatched = areParamsAssignable(candidate.functionType().params(), paramTypes);
            boolean retMatched = candidate.functionType().ret().isAssignableFrom(retType);
            if (paramsMatched && retMatched) {
                impls.add(candidate);
            }
        }
        return impls;
    }

    @NotNull
    public List<Denotation.FunctionType> resolveFunctions(DexFunctionCallExpr callExpr) {
        DexReference ref = callExpr.target().asRef();
        String functionName = ref.toString();
        List<Denotation.Type> argTypes = new ArrayList<>();
        for (DexExpr arg : callExpr.args()) {
            argTypes.add(resolve.resolveType(arg));
        }
        return resolveFunctions(functionName, argTypes);
    }

    @NotNull
    public List<Denotation.FunctionType> resolveFunctions(DexMethodCallExpr callExpr) {
        DexReference ref = callExpr.method();
        String functionName = ref.toString();
        List<Denotation.Type> argTypes = new ArrayList<>();
        argTypes.add(resolve.resolveType(callExpr.obj()));
        for (DexExpr arg : callExpr.args()) {
            argTypes.add(resolve.resolveType(arg));
        }
        return resolveFunctions(functionName, argTypes);
    }

    @NotNull
    public List<Denotation.FunctionType> resolveFunctions(DexAddExpr addExpr) {
        List<Denotation.Type> argTypes = new ArrayList<>();
        argTypes.add(resolve.resolveType(addExpr.left()));
        argTypes.add(resolve.resolveType(addExpr.right()));
        return resolveFunctions("Add__", argTypes);
    }

    @NotNull
    public List<Denotation.FunctionType> resolveFunctions(DexConsumeExpr consumeExpr) {
        List<Denotation.Type> argTypes = new ArrayList<>();
        argTypes.add(resolve.resolveType(consumeExpr.right()));
        return resolveFunctions("Consume__", argTypes);
    }

    private List<Denotation.FunctionType> resolveFunctions(String functionName, List<Denotation.Type> paramTypes) {
        List<Denotation.FunctionType> resolved = new ArrayList<>();
        if (concreteDefs.containsKey(functionName)) {
            List<Denotation.Function> candidates = concreteDefs.get(functionName);
            for (Denotation.Function candidate : candidates) {
                if (areParamsAssignable(paramTypes, candidate.functionType().params())) {
                    resolved.add(candidate.functionType());
                }
            }
        }
        if (virtualDefs.containsKey(functionName)) {
            List<Denotation.FunctionType> candidates = virtualDefs.get(functionName);
            for (Denotation.FunctionType candidate : candidates) {
                if (areParamsAssignable(paramTypes, candidate.params())) {
                    resolved.add(candidate);
                }
            }
        }
        return resolved;
    }

    private boolean areParamsAssignable(List<Denotation.Type> argTypes, List<Denotation.Type> paramTypes) {
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
        if (concreteDefs.containsKey(functionName)) {
            List<Denotation.Function> candidates = concreteDefs.get(functionName);
            for (Denotation.Function candidate : candidates) {
                if (candidate.functionType().canProvide(functionName, params, ret)) {
                    return true;
                }
            }
        }
        if (virtualDefs.containsKey(functionName)) {
            List<Denotation.FunctionType> candidates = virtualDefs.get(functionName);
            for (Denotation.FunctionType candidate : candidates) {
                if (candidate.canProvide(functionName, params, ret)) {
                    return true;
                }
            }
        }
        return false;
    }

    public Denotation.Type resolveType(DexFunction function) {
        Denotation.ActorType type = function.attachmentOfType(Denotation.ActorType.class);
        if (type != null) {
            return type;
        }
        type = new Denotation.ActorType(resolve, function);
        function.attach(type);
        for (Denotation.FunctionType member : type.members()) {
            define(member);
        }
        return type;
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
            return returnTypeOf(resolveFunctions((DexFunctionCallExpr)expr));
        }
        if (expr instanceof DexMethodCallExpr) {
            return returnTypeOf(resolveFunctions((DexMethodCallExpr)expr));
        }
        if (expr instanceof DexAddExpr) {
            return returnTypeOf(resolveFunctions((DexAddExpr)expr));
        }
        if (expr instanceof DexNewExpr) {
            return eval((DexNewExpr)expr);
        }
        if (expr instanceof DexConsumeExpr) {
            return returnTypeOf(resolveFunctions((DexConsumeExpr)expr));
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

    private Denotation.Type eval(DexNewExpr newExpr) {
        List<Denotation.Function> functions = resolveFunctions(newExpr);
        if (functions.size() == 0) {
            return BuiltinTypes.UNDEFINED_TYPE;
        }
        if (functions.size() == 1) {
            DexFunction definedBy = functions.get(0).definedBy();
            return resolveType(definedBy);
        }
        throw new UnsupportedOperationException("not implemented: intersection type of multiple functions");
    }
}
