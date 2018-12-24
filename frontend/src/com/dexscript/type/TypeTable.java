package com.dexscript.type;

import com.dexscript.ast.DexPackage;
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
            };

    public final static OnGenericTypeArgumentNotAssignable ON_GENERIC_TYPE_ARGUMENT_NOT_ASSIGNABLE =
            (name, genericType, typeArgs, i) -> {
            };

    private final TypeSystem ts;
    // package => type name => type
    private final Map<DexPackage, Map<String, DType>> defined = new HashMap<>();
    // package => providers
    private final Map<DexPackage, List<NamedTypesProvider>> providers = new HashMap<>();
    private final Map<Expansion, DType> expanded = new HashMap<>();

    public TypeTable(TypeSystem ts) {
        this.ts = ts;
    }

    public TypeTable(TypeSystem ts, List<DexTypeParam> typeParams) {
        this.ts = ts;
        for (DexTypeParam typeParam : typeParams) {
            DType type = ResolveType.$(ts, null, typeParam.paramType());
            define(typeParam.pkg(), typeParam.paramName().toString(), type);
        }
    }

    public DType resolveType(DexPackage pkg, String name) {
        pullFromProviders(pkg);
        Map<String, DType> pkgTypes = defined.get(pkg);
        if (pkgTypes == null) {
            return ts.UNDEFINED;
        }
        DType type = pkgTypes.get(name);
        if (type == null) {
            return ts.UNDEFINED;
        }
        return type;
    }

    public DType resolveType(DexPackage pkg, String name, List<DType> typeArgs) {
        DType type = this.resolveType(pkg, name);
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

    private void pullFromProviders(DexPackage pkg) {
        List<NamedTypesProvider> pkgTypeProviders = providers.get(pkg);
        if (pkgTypeProviders == null) {
            return;
        }
        for (NamedTypesProvider provider : pkgTypeProviders) {
            for (NamedType type : provider.namedTypes()) {
                define(pkg, type);
            }
        }
        pkgTypeProviders.clear();
    }

    public void define(DexPackage pkg, NamedType type) {
        define(pkg, type.name(), type);
    }

    public void define(DexPackage pkg, String typeName, DType type) {
        Map<String, DType> pkgTypes = defined.computeIfAbsent(pkg, k -> new HashMap<>());
        if (pkgTypes.containsKey(typeName)) {
            throw new DexSyntaxException("redefine type: " + typeName);
        }
        pkgTypes.put(typeName, type);
    }

    public void lazyDefine(DexPackage pkg, NamedTypesProvider provider) {
        List<NamedTypesProvider> pkgTypeProviders = providers.computeIfAbsent(pkg, k -> new ArrayList<>());
        pkgTypeProviders.add(provider);
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

}
