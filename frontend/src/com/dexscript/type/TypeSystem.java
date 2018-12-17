package com.dexscript.type;

import com.dexscript.ast.DexInterface;

import java.util.List;

public class TypeSystem {

    private final TypeTable typeTable = new TypeTable(this);
    private final FunctionTable functionTable = new FunctionTable();
    private final TypeComparisonCache comparisonCache = new TypeComparisonCache();

    public final DType ANY = new AnyType(this);
    public final NamedType BOOL = new BoolType(this);
    public final NamedType STRING = new StringType(this);
    public final NamedType INT64 = new Int64Type(this);
    public final NamedType INT32 = new Int32Type(this);
    public final NamedType UINT8 = new UInt8Type(this);
    public final NamedType VOID = new VoidType(this);
    public final NamedType UNDEFINED = new UndefinedType(this);

    public TypeTable typeTable() {
        return typeTable;
    }

    public TypeComparisonCache comparisonCache() {
        return comparisonCache;
    }

    public void defineFunction(FunctionType function) {
        functionTable.define(function);
    }


    public InterfaceType defineInterface(DexInterface inf) {
        return new InterfaceType(this, inf);
    }

    public List<FunctionSig.Invoked> invoke(Invocation ivc) {
        return functionTable.invoke(ivc);
    }

    public void defineType(NamedType type) {
        typeTable.define(type);
    }

    public void lazyDefineFunctions(FunctionsType functionsType) {
        functionTable.lazyDefine(functionsType);
    }

    public void lazyDefineTypes(NamedTypesProvider typesProvider) {
        typeTable.lazyDefine(typesProvider);
    }

    public FunctionTable functionTable() {
        return functionTable;
    }
}
