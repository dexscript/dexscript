package com.dexscript.type;

import com.dexscript.ast.core.DexSyntaxException;

import java.util.*;

public class TopLevelTypeTable {

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
        throw new DexSyntaxException("type is not generic: " + name);
    };

    public final static OnNoSuchType ON_NO_SUCH_TYPE = name -> {
        throw new DexSyntaxException("no such type: " + name);
    };

    public final static OnGenericTypeArgumentsSizeMismatch ON_GENERIC_TYPE_ARGUMENTS_SIZE_MISMATCH =
            (name, genericType, typeArgs) -> {
                throw new DexSyntaxException(String.format(
                        "generic type %s parameters: %s, actual arguments: %s",
                        name, genericType.typeParameters(), typeArgs));
            };

    public final static OnGenericTypeArgumentNotAssignable ON_GENERIC_TYPE_ARGUMENT_NOT_ASSIGNABLE =
            (name, genericType, typeArgs, i) -> {
                throw new DexSyntaxException(String.format(
                        "generic type %s #%d parameter: %s, actual argument: %s",
                        name, i + 1, genericType.typeParameters().get(i), typeArgs.get(i)));
            };

    private final Map<String, Type> defined = new HashMap<>();
    private final Map<Expansion, Type> expanded = new HashMap<>();
    private final List<TopLevelTypesProvider> providers = new ArrayList<>();
    private final Map<String, Type> javaTypes = new HashMap<>();

    public TopLevelTypeTable() {
    }

    public TopLevelTypeTable(TopLevelTypeTable copiedFrom) {
        defined.putAll(copiedFrom.defined);
        javaTypes.putAll(copiedFrom.javaTypes);
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
            if (!typeParam.isAssignableFrom(typeArg)) {
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
        for (TopLevelTypesProvider provider : providers) {
            for (TopLevelType type : provider.topLevelTypes()) {
                define(type);
            }
        }
        providers.clear();
    }

    public void define(TopLevelType type) {
        defined.put(type.name(), type);
        javaTypes.put(type.javaClassName(), type);
    }

    public void define(String typeName, Type type) {
        defined.put(typeName, type);
        javaTypes.put(type.javaClassName(), type);
    }

    public void lazyDefine(TopLevelTypesProvider provider) {
        providers.add(provider);
    }

    public Type resolveType(Class<?> javaType) {
        if (javaType.isArray()) {
            Type arrayElem = resolveType(javaType.getComponentType());
            return resolveType("Array", Arrays.asList(arrayElem));
        }
        String javaClassName = javaType.getCanonicalName();
        Type type = javaTypes.get(javaClassName);
        if (type == null) {
            throw new DexSyntaxException(javaClassName + " has not been imported");
        }
        return type;
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
}
