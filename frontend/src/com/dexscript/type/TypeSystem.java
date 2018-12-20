package com.dexscript.type;

import com.dexscript.ast.DexInterface;

import java.util.Arrays;
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

    public DType literalOf(long val) {
        return new IntegerLiteralType(this, String.valueOf(val));
    }

    public DType constOf(long val) {
        return new IntegerConstType(this, String.valueOf(val));
    }

    public DType literalOf(String val) {
        return new StringLiteralType(this, val);
    }

    public DType constOf(String val) {
        return new StringConstType(this, val);
    }

    public DType constOf(double val) {
        return new FloatConstType(this, String.valueOf(val));
    }

    public DType literalOf(boolean val) {
        return new BoolLiteralType(this, String.valueOf(val));
    }

    public DType constOf(boolean val) {
        return new BoolConstType(this, String.valueOf(val));
    }

    public DType constOfFloat(String val) {
        return new FloatConstType(this, val);
    }

    public DType constOfInteger(String val) {
        return new IntegerConstType(this, val);
    }

    public DType literalOfInteger(String val) {
        return new IntegerLiteralType(this, val);
    }

    public DType constOfBool(String val) {
        return new BoolConstType(this, val);
    }

    public DType literalOfBool(String val) {
        return new BoolLiteralType(this, val);
    }

    public boolean isFloatConst(DType type) {
        return type instanceof FloatConstType && type.typeSystem().equals(this);
    }

    public boolean isIntegerConst(DType type) {
        return type instanceof IntegerConstType && type.typeSystem().equals(this);
    }

    public boolean isFloat32(DType type) {
        return type instanceof Float32Type && type.typeSystem().equals(this);
    }

    public boolean isInt32(DType type) {
        return type instanceof Int32Type && type.typeSystem().equals(this);
    }

    public boolean isFloat64(DType type) {
        return type instanceof Float64Type && type.typeSystem().equals(this);
    }

    public boolean isInt64(DType type) {
        return type instanceof Int64Type && type.typeSystem().equals(this);
    }

    public boolean isIntegerLiteral(DType type) {
        return type instanceof IntegerLiteralType && type.typeSystem().equals(this);
    }

    public boolean isBoolConst(DType type) {
        return type instanceof BoolConstType && type.typeSystem().equals(this);
    }

    public boolean isStringConst(DType type) {
        return type instanceof StringConstType && type.typeSystem().equals(this);
    }

    public List<DType> widen(DType type) {
        if (type instanceof BoolConstType) {
            String val = ((BoolConstType) type).constValue();
            return Arrays.asList(literalOfBool(val), BOOL);
        }
        if (type instanceof StringConstType) {
            String val = ((StringConstType) type).constValue();
            return Arrays.asList(literalOf(val), STRING);
        }
        if (type instanceof IntegerConstType) {
            String val = ((IntegerConstType) type).constValue();
            return Arrays.asList(literalOfInteger(val), INT64, INT32, FLOAT64, FLOAT32);
        }
        if (type instanceof FloatConstType) {
            return Arrays.asList(FLOAT64, FLOAT32);
        }
        return Arrays.asList(type);
    }
}
