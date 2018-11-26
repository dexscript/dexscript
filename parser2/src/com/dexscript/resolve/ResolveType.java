package com.dexscript.resolve;

import com.dexscript.ast.expr.*;

final class ResolveType {

    private final DenotationTable builtin;
    private ResolveFunction resolveFunction;

    ResolveType(DenotationTable builtin) {
        this.builtin = builtin;
    }

    ResolveType() {
        this(BuiltinTypes.BUILTIN_TYPES);
    }

    public void setResolveFunction(ResolveFunction resolveFunction) {
        this.resolveFunction = resolveFunction;
    }

    public Denotation __(DexReference ref) {
        Denotation type = ref.attachmentOfType(Denotation.Type.class);
        if (type != null) {
            return type;
        }
        String refName = ref.toString();
        type = builtin.get(refName);
        if (type == null) {
            type = new Denotation.Error(refName, ref, "can not resolve " + refName + " to a type");
        }
        ref.attach(type);
        return type;
    }

    public Denotation __(DexExpr expr) {
        Denotation denotation = expr.attachmentOfType(Denotation.class);
        if (denotation != null) {
            return denotation;
        }
        denotation = resolveType(expr);
        expr.attach(denotation);
        return denotation;
    }

    private Denotation resolveType(DexExpr expr) {
        if (expr instanceof DexIntegerLiteral) {
            return BuiltinTypes.INT64_TYPE;
        }
        if (expr instanceof DexFloatLiteral) {
            return BuiltinTypes.FLOAT64_TYPE;
        }
        if (expr instanceof DexStringLiteral) {
            return BuiltinTypes.STRING_TYPE;
        }
        if (expr instanceof DexCallExpr) {
            Denotation typeObj = resolveFunction.__(((DexCallExpr) expr).target().asRef());
            if (typeObj instanceof Denotation.Type) {
                return ((Denotation.Type)typeObj).ret();
            }
            return BuiltinTypes.UNDEFINED_TYPE;
        }
        if (expr instanceof DexAddExpr) {
            Denotation typeObj = resolveFunction.__((DexAddExpr) expr);
            if (typeObj instanceof Denotation.Type) {
                return ((Denotation.Type)typeObj).ret();
            }
            return BuiltinTypes.UNDEFINED_TYPE;
        }
        return new Denotation.Error(expr.toString(), expr, "can not evaluate expression type");
    }
}
