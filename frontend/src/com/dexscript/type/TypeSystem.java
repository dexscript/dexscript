package com.dexscript.type;

import com.dexscript.ast.DexInterface;
import com.dexscript.ast.type.DexType;

import java.util.ArrayList;
import java.util.List;

public class TypeSystem {

    private final TypeTable typeTable = new TypeTable(BuiltinTypes.TYPE_TABLE);
    private final FunctionTable functionTable = new FunctionTable();

    public void defineFunction(FunctionType function) {
        functionTable.define(function);
    }

    public TypeTable typeTable() { return typeTable; }

    public InterfaceType defineInterface(DexInterface inf) {
        return new InterfaceType(typeTable, functionTable, inf);
    }

    public List<FunctionType.Invoked> invoke(String funcName, List<Type> typeArgs, List<Type> args, Type retHint) {
        return functionTable.invoke(typeTable, funcName, typeArgs, args, retHint);
    }

    public Type resolveType(String typeName) {
        return typeTable.resolveType(typeName);
    }

    public Type resolveType(String typeName, List<Type> typeArgs) {
        return typeTable.resolveType(typeName, typeArgs);
    }

    public Type resolveType(DexType elem) {
        return ResolveType.$(typeTable, elem);
    }

    public List<Type> resolveTypes(List<DexType> dexTypes) {
        List<Type> types = new ArrayList<>();
        for (DexType dexType : dexTypes) {
            types.add(resolveType(dexType));
        }
        return types;
    }

    public List<Type> resolveType(Class<?>[] javaTypes) {
        List<Type> types = new ArrayList<>();
        for (Class<?> javaType : javaTypes) {
            types.add(resolveType(javaType));
        }
        return types;
    }

    public Type resolveType(Class<?> javaType) {
        return typeTable.resolveType(javaType);
    }

    public void defineType(NamedType type) {
        typeTable.define(type);
    }

    public void lazyDefineFunctions(FunctionsProvider functionsProvider) {
        functionTable.lazyDefine(functionsProvider);
    }

    public boolean isFunctionDefined(TypeComparisonContext ctx, FunctionType functionType) {
        return functionTable.isDefined(ctx, functionType);
    }

    public void lazyDefineTypes(NamedTypesProvider typesProvider) {
        typeTable.lazyDefine(typesProvider);
    }
}
