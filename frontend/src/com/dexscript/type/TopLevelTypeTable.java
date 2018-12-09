package com.dexscript.type;

import com.dexscript.ast.core.DexSyntaxException;

import java.util.*;

public class TopLevelTypeTable {

    private final Map<String, TopLevelType> defined = new HashMap<>();
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
        TopLevelType type = defined.get(name);
        if (type == null) {
            return BuiltinTypes.UNDEFINED;
        }
        return type;
    }

    public Type resolveType(String name, List<Type> typeArgs) {
        Type type = this.resolveType(name);
        if (!(type instanceof GenericType)) {
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
            throw new DexSyntaxException(String.format(
                    "generic type %s parameters: %s, actual arguments: %s",
                    name, typeParams, typeArgs));
        }
        for (int i = 0; i < typeParams.size(); i++) {
            Type typeParam = typeParams.get(i);
            Type typeArg = typeArgs.get(i);
            if (!typeParam.isAssignableFrom(typeArg)) {
                throw new DexSyntaxException(String.format(
                        "generic type %s #%d parameter: %s, actual argument: %s",
                        name, i + 1, typeParam, typeArg));
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

    public void lazyDefine(TopLevelTypesProvider provider) {
        providers.add(provider);
    }

    public Type resolveType(Class<?> javaType) {
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
