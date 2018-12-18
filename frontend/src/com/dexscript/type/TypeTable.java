package com.dexscript.type;

import com.dexscript.ast.core.DexSyntaxException;
import com.dexscript.ast.elem.DexTypeParam;

import java.util.*;

public class TypeTable {

    public interface OnNotGenericType {
        void handle(String name, List<DType> typeArgs, DType actualType);
    }

    public interface OnGenericTypeArgumentsSizeMismatch {
        void handle(String name, GenericType genericType, List<DType> typeArgs);
    }

    public interface OnGenericTypeArgumentNotAssignable {
        void handle(String name, GenericType genericType, List<DType> typeArgs, int i);
    }

    public interface OnNoSuchType {
        void handle(String name);
    }

    public final static OnNotGenericType ON_NOT_GENERIC_TYPE = (name, typeArgs, actualType) -> {
        throw new DexSyntaxException("paramType is not generic: " + name);
    };

    public final static OnNoSuchType ON_NO_SUCH_TYPE = name -> {
    };

    public final static OnGenericTypeArgumentsSizeMismatch ON_GENERIC_TYPE_ARGUMENTS_SIZE_MISMATCH =
            (name, genericType, typeArgs) -> {
                throw new DexSyntaxException(String.format(
                        "generic paramType %s parameters: %s, actual arguments: %s",
                        name, genericType.typeParameters(), typeArgs));
            };

    public final static OnGenericTypeArgumentNotAssignable ON_GENERIC_TYPE_ARGUMENT_NOT_ASSIGNABLE =
            (name, genericType, typeArgs, i) -> {
                throw new DexSyntaxException(String.format(
                        "generic paramType %s #%d parameter: %s, actual argument: %s",
                        name, i + 1, genericType.typeParameters().get(i), typeArgs.get(i)));
            };

    private final TypeSystem ts;
    private final Map<String, DType> defined = new HashMap<>();
    private final Map<Expansion, DType> expanded = new HashMap<>();
    private final List<NamedTypesProvider> providers = new ArrayList<>();
    private final TypeComparisonCache cache = new TypeComparisonCache();

    public TypeTable(TypeSystem ts) {
        this.ts = ts;
    }

    public TypeTable(TypeSystem ts, List<DexTypeParam> typeParams) {
        this.ts = ts;
        for (DexTypeParam typeParam : typeParams) {
            DType type = ResolveType.$(ts, null, typeParam.paramType());
            define(typeParam.paramName().toString(), type);
        }
    }

    public DType resolveType(String name) {
        pullFromProviders();
        DType type = defined.get(name);
        if (type == null) {
            ON_NO_SUCH_TYPE.handle(name);
            return ts.UNDEFINED;
        }
        return type;
    }

    public DType resolveType(String name, List<DType> typeArgs) {
        DType type = this.resolveType(name);
        if (type == null) {
            ON_NO_SUCH_TYPE.handle(name);
            return ts.UNDEFINED;
        }
        if (!(type instanceof GenericType)) {
            ON_NOT_GENERIC_TYPE.handle(name, typeArgs, type);
            return ts.UNDEFINED;
        }
        GenericType genericType = (GenericType) type;
        Expansion expansion = new Expansion(genericType, typeArgs);
        DType expandedType = expanded.get(expansion);
        if (expandedType != null) {
            return expandedType;
        }
        List<DType> typeParams = genericType.typeParameters();
        if (typeParams.size() != typeArgs.size()) {
            ON_GENERIC_TYPE_ARGUMENTS_SIZE_MISMATCH.handle(name, genericType, typeArgs);
            return ts.UNDEFINED;
        }
        for (int i = 0; i < typeParams.size(); i++) {
            DType typeParam = typeParams.get(i);
            DType typeArg = typeArgs.get(i);
            if (!IsAssignable.$(typeParam, typeArg)) {
                ON_GENERIC_TYPE_ARGUMENT_NOT_ASSIGNABLE.handle(name, genericType, typeArgs, i);
                return ts.UNDEFINED;
            }
        }
        expandedType = genericType.generateType(typeArgs);
        expanded.put(expansion, expandedType);
        return expandedType;
    }

    private void pullFromProviders() {
        if (providers.isEmpty()) {
            return;
        }
        for (NamedTypesProvider provider : providers) {
            for (NamedType type : provider.namedTypes()) {
                define(type);
            }
        }
        providers.clear();
    }

    public void define(NamedType type) {
        defined.put(type.name(), type);
    }

    public void define(String typeName, DType type) {
        defined.put(typeName, type);
    }

    public void lazyDefine(NamedTypesProvider provider) {
        providers.add(provider);
    }

    private static class Expansion {

        private final GenericType genericType;
        private final List<DType> typeArgs;

        private Expansion(GenericType genericType, List<DType> typeArgs) {
            this.genericType = genericType;
            this.typeArgs = typeArgs;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Expansion expansion = (Expansion) o;
            return Objects.equals(genericType, expansion.genericType) &&
                    Objects.equals(typeArgs, expansion.typeArgs);
        }

        @Override
        public int hashCode() {
            return Objects.hash(genericType, typeArgs);
        }
    }

    public TypeComparisonCache comparisonCache() {
        return cache;
    }
}
