package com.dexscript.type;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TaskType implements NamedType, FunctionsProvider, GenericType {

    private final static List<Type> TYPE_PARAMETERS = Arrays.asList(BuiltinTypes.ANY);
    private final @NotNull FunctionType resolveFunc;
    private final TypeTable typeTable;
    private final FunctionTable functionTable;

    public TaskType(@NotNull TypeTable typeTable, @NotNull FunctionTable functionTable) {
        this(typeTable, functionTable, TYPE_PARAMETERS);
    }

    public TaskType(@NotNull TypeTable typeTable, @NotNull FunctionTable functionTable, List<Type> typeArgs) {
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
    public @NotNull String name() {
        return "Task";
    }

    @Override
    public String javaClassName() {
        return "Actor";
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
