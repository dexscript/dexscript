package com.dexscript.type;

import com.dexscript.ast.DexInterface;
import com.dexscript.ast.DexPackage;
import com.dexscript.ast.elem.DexParam;
import com.dexscript.ast.inf.DexInfMethod;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TypeSystem {

    private final TypeTable typeTable = new TypeTable(this);
    private final FunctionTable functionTable = new FunctionTable();
    private final TypeComparisonCache comparisonCache = new TypeComparisonCache();

    public final DType ANY = new AnyType(this);
    public final NamedType BOOL = new BoolType(this);
    public final NamedType STRING = new StringType(this);
    public final NamedType FLOAT64 = new Float64Type(this);
    public final NamedType FLOAT32 = new Float32Type(this);
    public final NamedType INT64 = new Int64Type(this);
    public final NamedType INT32 = new Int32Type(this);
    public final NamedType UINT8 = new UInt8Type(this);
    public final NamedType VOID = new VoidType(this);
    public final NamedType UNDEFINED = new UndefinedType(this);

    public TypeSystem() {
        defineBuiltinTypes(DexPackage.DUMMY);
    }

    public TypeTable typeTable() {
        return typeTable;
    }

    public TypeComparisonCache comparisonCache() {
        return comparisonCache;
    }

    public void defineBuiltinTypes(DexPackage pkg) {
        typeTable.define(pkg, BOOL);
        typeTable.define(pkg, STRING);
        typeTable.define(pkg, FLOAT64);
        typeTable.define(pkg, FLOAT32);
        typeTable.define(pkg, INT64);
        typeTable.define(pkg, INT32);
        typeTable.define(pkg, UINT8);
        typeTable.define(pkg, VOID);
        typeTable.define(pkg, UNDEFINED);
    }

    public void defineFunction(FunctionType function) {
        functionTable.define(function);
    }

    public void defineGlobalSPI(DexInterface inf) {
        for (DexInfMethod infMethod : inf.methods()) {
            String name = infMethod.identifier().toString();
            List<FunctionParam> params = new ArrayList<>();
            for (DexParam param : infMethod.sig().params()) {
                String paramName = param.paramName().toString();
                DType paramType = ResolveType.$(this, null, param.paramType());
                params.add(new FunctionParam(paramName, paramType));
            }
            DType ret = ResolveType.$(this, null, infMethod.sig().ret());
            this.defineFunction(new FunctionType(this, name, params, ret));
        }
    }

    public InterfaceType defineInterface(DexInterface inf) {
        return new InterfaceType(this, inf);
    }

    public Invoked invoke(Invocation ivc) {
        return functionTable.invoke(ivc);
    }

    public void defineType(DexPackage pkg, NamedType type) {
        typeTable.define(pkg, type);
    }

    public void lazyDefineFunctions(FunctionsType functionsType) {
        functionTable.lazyDefine(functionsType);
    }

    public void lazyDefineTypes(DexPackage pkg, NamedTypesProvider typesProvider) {
        typeTable.lazyDefine(pkg, typesProvider);
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
}
