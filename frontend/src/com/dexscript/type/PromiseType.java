package com.dexscript.type;


import org.jetbrains.annotations.NotNull;

import java.util.*;

public class PromiseType implements NamedType, FunctionsProvider, GenericType {

    private final static List<Type> TYPE_PARAMETERS = Arrays.asList(BuiltinTypes.ANY);
    private final @NotNull FunctionType consumeFunc;
    private final TypeTable typeTable;
    private final FunctionTable functionTable;

    public PromiseType(@NotNull TypeTable typeTable, @NotNull FunctionTable functionTable) {
        this(typeTable, functionTable, TYPE_PARAMETERS);
    }

    public PromiseType(@NotNull TypeTable typeTable, @NotNull FunctionTable functionTable, List<Type> typeArgs) {
        this.typeTable = typeTable;
        this.functionTable = functionTable;
        typeTable.define(this);
        functionTable.lazyDefine(this);
        consumeFunc = consumeFunc(typeArgs);
    }

    @Override
    public String javaClassName() {
        return "Promise";
    }

    @Override
    public @NotNull String name() {
        return "Promise";
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
    public boolean _isSubType(TypeComparisonContext ctx, Type that) {
        ctx.putSubstituted(this, that);
        for (FunctionType member : functions()) {
            if (!functionTable.isDefined(ctx, member)) {
                return false;
            }
        }
        return true;
    }
}
