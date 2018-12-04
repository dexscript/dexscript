package com.dexscript.type;

import com.dexscript.ast.DexFile;
import com.dexscript.ast.DexFunction;
import com.dexscript.ast.DexInterface;
import com.dexscript.ast.DexTopLevelDecl;
import com.dexscript.ast.expr.DexReference;

import java.util.List;

public class TypeSystem {

    private final TopLevelTypeTable typeTable = new TopLevelTypeTable(BuiltinTypes.TYPE_TABLE);
    private final ActorTable actorTable = new ActorTable(typeTable);
    private final FunctionTable functionTable = new FunctionTable();

    public void defineFunction(FunctionType function) {
        functionTable.define(function);
    }

    public ActorType defineActor(DexFunction elem) {
        return new ActorType(actorTable, functionTable, elem);
    }

    public InterfaceType defineInterface(DexInterface inf) {
        return new InterfaceType(typeTable, functionTable, inf);
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

    public void defineFile(DexFile file) {
        for (DexTopLevelDecl topLevelDecl : file.topLevelDecls()) {
            if (topLevelDecl.function() != null) {
                defineActor(topLevelDecl.function());
            } else if (topLevelDecl.inf() != null) {
                defineInterface(topLevelDecl.inf());
            }
        }
    }
}
