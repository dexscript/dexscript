package com.dexscript.type;

import com.dexscript.ast.DexInterface;

import java.util.List;

public class TypeSystem {

    private final TypeTable typeTable = new TypeTable(this);
    private final FunctionTable functionTable = new FunctionTable();
    private final TypeComparisonCache comparisonCache = new TypeComparisonCache();

    public final DType ANY = new AnyType(this);
    public final DType BOOL = new BoolType(this);
    public final DType STRING = new StringType(this);
    public final DType FLOAT64 = new Float64Type(this);
    public final DType FLOAT32 = new Float32Type(this);
    public final DType INT64 = new Int64Type(this);
    public final DType INT32 = new Int32Type(this);
    public final DType UINT8 = new UInt8Type(this);
    public final DType VOID = new VoidType(this);
    public final DType UNDEFINED = new UndefinedType(this);

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

    public IntegerLiteralType literalOf(long val) {
        return new IntegerLiteralType(this, String.valueOf(val));
    }

    public IntegerConstType constOf(long val) {
        return new IntegerConstType(this, String.valueOf(val));
    }

    public StringLiteralType literalOf(String val) {
        return new StringLiteralType(this, val);
    }

    public StringConstType constOf(String val) {
        return new StringConstType(this, val);
    }

    public FloatConstType constOf(double val) {
        return new FloatConstType(this, String.valueOf(val));
    }
}
