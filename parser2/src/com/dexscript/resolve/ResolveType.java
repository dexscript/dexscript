package com.dexscript.resolve;

import com.dexscript.ast.DexFunction;
import com.dexscript.ast.expr.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

final class ResolveType {

    private final Map<String, List<DexFunction>> declaredFunctions = new HashMap<>();
    private final DenotationTable declared;
    private Resolve resolve;

    ResolveType(DenotationTable builtin) {
        this.declared = builtin;
    }

    ResolveType() {
        this(BuiltinTypes.BUILTIN_TYPES);
    }

    public void setResolve(Resolve resolve) { this.resolve = resolve; }

    public void declare(Denotation.InterfaceType inf) {
        declared.put(inf.name(), inf);
    }

    public Denotation resolveType(DexReference ref) {
        Denotation type = ref.attachmentOfType(Denotation.Type.class);
        if (type != null) {
            return type;
        }
        String refName = ref.toString();
        type = declared.get(refName);
        if (type != null) {
            ref.attach(type);
            return type;
        }
        List<DexFunction> functions = declaredFunctions.get(refName);
        if (functions != null) {
            for (DexFunction function : functions) {
                return new Denotation.FunctionInterfaceType(resolve, function);
            }
        }
        type = new Denotation.Error(refName, ref, "can not resolve " + refName + " to a type");
        return type;
    }

    public Denotation resolveType(DexExpr expr) {
        Denotation denotation = expr.attachmentOfType(Denotation.Type.class);
        if (denotation != null) {
            return denotation;
        }
        denotation = _resolveType(expr);
        expr.attach(denotation);
        return denotation;
    }

    private Denotation _resolveType(DexExpr expr) {
        if (expr instanceof DexIntegerLiteral) {
            return BuiltinTypes.INT64_TYPE;
        }
        if (expr instanceof DexFloatLiteral) {
            return BuiltinTypes.FLOAT64_TYPE;
        }
        if (expr instanceof DexStringLiteral) {
            return BuiltinTypes.STRING_TYPE;
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
        if (expr instanceof DexReference) {
            return eval((DexReference)expr);
        }
        return new Denotation.Error(expr.toString(), expr, "can not evaluate expression type: " + expr.getClass());
    }

    private Denotation eval(DexReference expr) {
        Denotation refObj = resolve.resolveValue(expr);
        if (refObj instanceof Denotation.Value) {
            Denotation.Value ref = (Denotation.Value) refObj;
            return ref.type();
        }
        return BuiltinTypes.UNDEFINED_TYPE;
    }

    private Denotation eval(DexFunctionCallExpr functionCallExpr) {
        Denotation typeObj = resolve.resolveFunction(functionCallExpr);
        if (typeObj instanceof Denotation.FunctionType) {
            return ((Denotation.FunctionType) typeObj).ret();
        }
        return BuiltinTypes.UNDEFINED_TYPE;
    }

    private Denotation eval(DexMethodCallExpr methodCallExpr) {
        Denotation typeObj = resolve.resolveFunction(methodCallExpr);
        if (typeObj instanceof Denotation.FunctionType) {
            return ((Denotation.FunctionType) typeObj).ret();
        }
        return BuiltinTypes.UNDEFINED_TYPE;
    }

    private Denotation eval(DexAddExpr addExpr) {
        Denotation typeObj = resolve.resolveFunction(addExpr);
        if (typeObj instanceof Denotation.FunctionType) {
            return ((Denotation.FunctionType) typeObj).ret();
        }
        return BuiltinTypes.UNDEFINED_TYPE;
    }

    private Denotation eval(DexNewExpr newExpr) {
        Denotation typeObj = resolve.resolveFunction(newExpr);
        if (typeObj instanceof Denotation.FunctionType) {
            DexFunction definedBy = (DexFunction) ((Denotation.FunctionType) typeObj).definedBy();
            return new Denotation.FunctionInterfaceType(resolve, definedBy);
        }
        return BuiltinTypes.UNDEFINED_TYPE;
    }

    public void declare(DexFunction function) {
        String functionName = function.identifier().toString();
        List<DexFunction> functions = declaredFunctions.computeIfAbsent(functionName, k -> new ArrayList<>());
        functions.add(function);
    }
}
