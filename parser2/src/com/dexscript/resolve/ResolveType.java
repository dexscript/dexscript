package com.dexscript.resolve;

import com.dexscript.ast.expr.*;

final class ResolveType {

    private final DenotationTable declared;
    private ResolveFunction resolveFunction;
    private ResolveValue resolveValue;

    ResolveType(DenotationTable builtin) {
        this.declared = builtin;
    }

    ResolveType() {
        this(BuiltinTypes.BUILTIN_TYPES);
    }

    public void setResolveFunction(ResolveFunction resolveFunction) {
        this.resolveFunction = resolveFunction;
    }

    public void setResolveValue(ResolveValue resolveValue) {
        this.resolveValue = resolveValue;
    }

    public void declare(Denotation.InterfaceType inf) {
        declared.put(inf.name(), inf);
        for (Denotation.FunctionType functionType : inf.members()) {
            resolveFunction.declare(functionType);
        }
    }

    public Denotation resolveType(DexReference ref) {
        Denotation type = ref.attachmentOfType(Denotation.Type.class);
        if (type != null) {
            return type;
        }
        String refName = ref.toString();
        type = declared.get(refName);
        if (type == null) {
            type = new Denotation.Error(refName, ref, "can not resolve " + refName + " to a type");
        }
        ref.attach(type);
        return type;
    }

    public Denotation resolveType(DexExpr expr) {
        Denotation denotation = expr.attachmentOfType(Denotation.class);
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
            Denotation typeObj = resolveFunction.resolveFunction((DexFunctionCallExpr) expr);
            if (typeObj instanceof Denotation.FunctionType) {
                return ((Denotation.FunctionType) typeObj).ret();
            }
            return BuiltinTypes.UNDEFINED_TYPE;
        }
        if (expr instanceof DexMethodCallExpr) {
            Denotation typeObj = resolveFunction.resolveFunction((DexMethodCallExpr) expr);
            if (typeObj instanceof Denotation.FunctionType) {
                return ((Denotation.FunctionType) typeObj).ret();
            }
            return BuiltinTypes.UNDEFINED_TYPE;
        }
        if (expr instanceof DexAddExpr) {
            Denotation typeObj = resolveFunction.resolveFunction((DexAddExpr) expr);
            if (typeObj instanceof Denotation.FunctionType) {
                return ((Denotation.FunctionType) typeObj).ret();
            }
            return BuiltinTypes.UNDEFINED_TYPE;
        }
        if (expr instanceof DexReference) {
            Denotation refObj = resolveValue.resolveValue(((DexReference) expr));
            if (refObj instanceof Denotation.Value) {
                Denotation.Value ref = (Denotation.Value) refObj;
                return ref.type();
            }
            return BuiltinTypes.UNDEFINED_TYPE;
        }
        return new Denotation.Error(expr.toString(), expr, "can not evaluate expression type: " + expr.getClass());
    }
}
