package com.dexscript.type;


import org.jetbrains.annotations.NotNull;

import java.util.*;

public class PromiseType extends NamedType implements FunctionsProvider, GenericType {

    private final static List<Type> TYPE_PARAMETERS = Arrays.asList(BuiltinTypes.ANY);
    private final @NotNull FunctionType consumeFunc;
    private final TypeTable typeTable;
    private final FunctionTable functionTable;

    public PromiseType(@NotNull TypeTable typeTable, @NotNull FunctionTable functionTable) {
        this(typeTable, functionTable, TYPE_PARAMETERS);
    }

    public PromiseType(@NotNull TypeTable typeTable, @NotNull FunctionTable functionTable, List<Type> typeArgs) {
        super("Promise", "Promise");
        this.typeTable = typeTable;
        this.functionTable = functionTable;
        typeTable.define(this);
        functionTable.lazyDefine(this);
        consumeFunc = consumeFunc(typeArgs);
    }

    @NotNull
    private FunctionType consumeFunc(List<Type> typeArgs) {
        ArrayList<Type> params = new ArrayList<>();
        params.add(this);
        return new FunctionType("Consume__", params, typeArgs.get(0));
    }

    @Override
    public List<FunctionType> functions() {
        return Arrays.asList(consumeFunc);
    }

    @Override
    public Type generateType(List<Type> typeArgs) {
        return new PromiseType(typeTable, functionTable, typeArgs);
    }

    @Override
    public List<Type> typeParameters() {
        return TYPE_PARAMETERS;
    }

    @Override
    public boolean isAssignableFrom(Substituted substituted, Type that) {
        if (super.isAssignableFrom(substituted, that)) {
            return true;
        }
        return functionTable.isAssignableFrom(substituted, this, that);
    }
}
