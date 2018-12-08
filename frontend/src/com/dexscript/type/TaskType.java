package com.dexscript.type;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TaskType extends TopLevelType implements FunctionsProvider {

    private final @NotNull FunctionType resolveFunc;

    public TaskType(@NotNull TopLevelTypeTable typeTable, @NotNull FunctionTable functionTable) {
        super("Task", "Object");
        typeTable.define(this);
        functionTable.lazyDefine(this);
        resolveFunc = resolveFunc();
    }

    @NotNull
    private FunctionType resolveFunc() {
        ArrayList<Type> params = new ArrayList<>();
        params.add(this);
        params.add(BuiltinTypes.ANY);
        return new FunctionType("Resolve__", params, BuiltinTypes.VOID);
    }

    @Override
    public List<FunctionType> functions() {
        return Arrays.asList(resolveFunc);
    }
}
