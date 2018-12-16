package com.dexscript.type;

import com.dexscript.ast.core.DexSyntaxException;
import com.dexscript.ast.elem.DexTypeParam;

import java.util.*;

public class TypeTable {

    public interface OnNotGenericType {
        void handle(String name, List<Type> typeArgs, Type actualType);
    }

    public interface OnGenericTypeArgumentsSizeMismatch {
        void handle(String name, GenericType genericType, List<Type> typeArgs);
    }

    public interface OnGenericTypeArgumentNotAssignable {
        void handle(String name, GenericType genericType, List<Type> typeArgs, int i);
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

    private final Map<String, Type> defined = new HashMap<>();
    private final Map<Expansion, Type> expanded = new HashMap<>();
    private final List<NamedTypesProvider> providers = new ArrayList<>();
    private final TypeComparisonCache cache = new TypeComparisonCache();

    public TypeTable() {
    }

    public TypeTable(TypeTable copiedFrom) {
        defined.putAll(copiedFrom.defined);
        providers.addAll(copiedFrom.providers);
        expanded.putAll(copiedFrom.expanded);
    }

    public Type resolveType(String name) {
        pullFromProviders();
        Type type = defined.get(name);
        if (type == null) {
            ON_NO_SUCH_TYPE.handle(name);
            return BuiltinTypes.UNDEFINED;
        }
        return type;
    }

    public Type resolveType(String name, List<Type> typeArgs) {
        Type type = this.resolveType(name);
        if (type == null) {
            ON_NO_SUCH_TYPE.handle(name);
            return BuiltinTypes.UNDEFINED;
        }
        if (!(type instanceof GenericType)) {
            ON_NOT_GENERIC_TYPE.handle(name, typeArgs, type);
            return BuiltinTypes.UNDEFINED;
        }
        GenericType genericType = (GenericType) type;
        Expansion expansion = new Expansion(genericType, typeArgs);
        Type expandedType = expanded.get(expansion);
        if (expandedType != null) {
            return expandedType;
        }
        List<Type> typeParams = genericType.typeParameters();
        if (typeParams.size() != typeArgs.size()) {
            ON_GENERIC_TYPE_ARGUMENTS_SIZE_MISMATCH.handle(name, genericType, typeArgs);
            return BuiltinTypes.UNDEFINED;
        }
        for (int i = 0; i < typeParams.size(); i++) {
            Type typeParam = typeParams.get(i);
            Type typeArg = typeArgs.get(i);
            if (!typeParam.isAssignableFrom(cache, typeArg)) {
                ON_GENERIC_TYPE_ARGUMENT_NOT_ASSIGNABLE.handle(name, genericType, typeArgs, i);
                return BuiltinTypes.UNDEFINED;
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

    public void define(String typeName, Type type) {
        defined.put(typeName, type);
    }

    public void define(List<DexTypeParam> typeParams) {
        for (DexTypeParam typeParam : typeParams) {
            define(typeParam.paramName().toString(), ResolveType.$(this, typeParam.paramType()));
        }
    }

    public void lazyDefine(NamedTypesProvider provider) {
        providers.add(provider);
    }

    private static class Expansion {

        private final GenericType genericType;
        private final List<Type> typeArgs;

        private Expansion(GenericType genericType, List<Type> typeArgs) {
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
