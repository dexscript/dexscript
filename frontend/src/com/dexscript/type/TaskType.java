package com.dexscript.type;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TaskType extends TopLevelType implements FunctionsProvider, GenericType {

    private final static List<Type> TYPE_PARAMETERS = Arrays.asList(BuiltinTypes.ANY);
    private final @NotNull FunctionType resolveFunc;
    private final TopLevelTypeTable typeTable;
    private final FunctionTable functionTable;

    public TaskType(@NotNull TopLevelTypeTable typeTable, @NotNull FunctionTable functionTable) {
        this(typeTable, functionTable, TYPE_PARAMETERS);
    }

    public TaskType(@NotNull TopLevelTypeTable typeTable, @NotNull FunctionTable functionTable, List<Type> typeArgs) {
        super("Task", "Actor");
        this.typeTable = typeTable;
        this.functionTable = functionTable;
        typeTable.define(this);
        functionTable.lazyDefine(this);
        resolveFunc = resolveFunc(typeArgs);
    }

    @NotNull
    private FunctionType resolveFunc(List<Type> typeArgs) {
        ArrayList<Type> params = new ArrayList<>();
        params.add(this);
        params.add(typeArgs.get(0));
        return new FunctionType("Resolve__", params, BuiltinTypes.VOID);
    }

    @Override
    public List<FunctionType> functions() {
        return Arrays.asList(resolveFunc);
    }

    @Override
    public Type generateType(List<Type> typeArgs) {
        return new TaskType(typeTable, functionTable, typeArgs);
    }

    @Override
    public List<Type> typeParameters() {
        return TYPE_PARAMETERS;
    }
}
