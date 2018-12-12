package com.dexscript.type;

import com.dexscript.ast.DexActor;
import com.dexscript.ast.DexInterface;
import com.dexscript.ast.elem.DexParam;
import com.dexscript.ast.type.DexType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TypeSystem {

    private final TypeTable typeTable = new TypeTable(BuiltinTypes.TYPE_TABLE);
    private final ActorTable actorTable = new ActorTable(typeTable);
    private final FunctionTable functionTable = new FunctionTable();

    public TypeSystem() {
        /*
        interface Task {
            <T>: interface{}
            Resolve__(value: T)
        }
         */
        new TaskType(typeTable, functionTable);
        /*
        interface Promise {
            <T>: interface{}
            Consume__(): T
        }
         */
        new PromiseType(typeTable, functionTable);
    }

    public void defineFunction(FunctionType function) {
        functionTable.define(function);
    }

    public ActorType defineActor(DexActor elem, ActorType.ImplProvider implProvider) {
        ActorType actorType = new ActorType(typeTable, functionTable, elem, implProvider);
        actorTable.define(actorType);
        return actorType;
    }

    public TypeTable typeTable() { return typeTable; }

    public InterfaceType defineInterface(DexInterface inf) {
        return new InterfaceType(typeTable, functionTable, inf);
    }

    public List<FunctionType> resolveFunctions(String funcName, List<Type> args) {
        return functionTable.resolve(funcName, args);
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

    public List<Type> resolveTypes(List<DexParam> params) {
        ArrayList<Type> types = new ArrayList<>();
        for (DexParam param : params) {
            types.add(resolveType(param.paramType()));
        }
        return types;
    }
}
