package com.dexscript.transpile.type.actor;

import com.dexscript.type.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TaskType implements NamedType, FunctionsProvider, GenericType {

    private final @NotNull FunctionType resolveFunc;
    private final TypeSystem ts;

    public TaskType(TypeSystem ts) {
        this(ts, null);
    }

    public TaskType(TypeSystem ts, List<DType> typeArgs) {
        this.ts = ts;
        if (typeArgs == null) {
            ts.defineType(this);
        }
        ts.lazyDefineFunctions(this);
        resolveFunc = resolveFunc(typeArgs == null ? Arrays.asList(ts.ANY) : typeArgs);
    }

    @NotNull
    private FunctionType resolveFunc(List<DType> typeArgs) {
        ArrayList<DType> params = new ArrayList<>();
        params.add(this);
        params.add(typeArgs.get(0));
        return new FunctionType(ts, "Resolve__", params, ts.VOID);
    }

    @Override
    public @NotNull String name() {
        return "Task";
    }

    @Override
    public List<FunctionType> functions() {
        return Arrays.asList(resolveFunc);
    }

    @Override
    public DType generateType(List<DType> typeArgs) {
        return new TaskType(ts, typeArgs);
    }

    @Override
    public List<DType> typeParameters() {
        return Arrays.asList(ts.ANY);
    }

    @Override
    public boolean _isSubType(TypeComparisonContext ctx, DType that) {
        return ts.functionTable().isSubType(ctx, this, that);
    }

    @Override
    public TypeSystem typeSystem() {
        return ts;
    }

    @Override
    public String toString() {
        return "Task";
    }
}
