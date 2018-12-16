package com.dexscript.type;

import com.dexscript.ast.DexInterface;
import com.dexscript.ast.type.DexType;

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

    public List<FunctionSig.Invoked> invoke(Invocation ivc) {
        return functionTable.invoke(typeTable, ivc);
    }

    public DType resolveType(String typeName) {
        return typeTable.resolveType(typeName);
    }

    public DType resolveType(String typeName, List<DType> typeArgs) {
        return typeTable.resolveType(typeName, typeArgs);
    }

    public DType resolveType(DexType elem) {
        return ResolveType.$(typeTable, elem);
    }

    public List<DType> resolveTypes(List<DexType> dexTypes) {
        return ResolveType.resolveTypes(typeTable, dexTypes);
    }

    public void defineType(NamedType type) {
        typeTable.define(type);
    }

    public void lazyDefineFunctions(FunctionsProvider functionsProvider) {
        functionTable.lazyDefine(functionsProvider);
    }

    public boolean isSubType(TypeComparisonContext ctx, FunctionsProvider assignedTo, DType assignedFrom) {
        return functionTable.isSubType(ctx, assignedTo, assignedFrom);
    }

    public void lazyDefineTypes(NamedTypesProvider typesProvider) {
        typeTable.lazyDefine(typesProvider);
    }
}
