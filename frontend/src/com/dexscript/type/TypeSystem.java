package com.dexscript.type;

import com.dexscript.ast.DexInterface;
import com.dexscript.ast.expr.DexReference;

import java.util.List;

public class TypeSystem {

    private final TopLevelTypeTable typeTable = new TopLevelTypeTable(BuiltinTypes.TYPE_TABLE);
    private final FunctionTable functionTable = new FunctionTable();

    public void defineFunction(FunctionType function) {
        functionTable.define(function);
    }

    public void defineInterface(DexInterface inf) {
        new InterfaceType(typeTable, functionTable, inf);
    }

    public Type resolveType(String typeName) {
        return typeTable.resolveType(typeName);
    }

    public Type resolveType(DexReference ref) {
        return resolveType(ref.toString());
    }

    public List<FunctionType> resolveFunctions(String funcName, List<Type> args) {
        return functionTable.resolve(funcName, args);
    }
}
