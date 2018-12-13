package com.dexscript.transpile.type.actor;

import com.dexscript.type.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TaskType implements NamedType, FunctionsProvider, GenericType {

    private final static List<Type> TYPE_PARAMETERS = Arrays.asList(BuiltinTypes.ANY);
    private final @NotNull FunctionType resolveFunc;
    private final TypeSystem ts;

    public TaskType(TypeSystem ts) {
        this(ts, null);
    }

    public TaskType(TypeSystem ts, List<Type> typeArgs) {
        this.ts = ts;
        if (typeArgs == null) {
            ts.defineType(this);
        }
        ts.lazyDefineFunctions(this);
        resolveFunc = resolveFunc(typeArgs == null ? TYPE_PARAMETERS : typeArgs);
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
    public List<FunctionType> functions() {
        return Arrays.asList(resolveFunc);
    }

    @Override
    public Type generateType(List<Type> typeArgs) {
        return new TaskType(ts, typeArgs);
    }

    @Override
    public List<Type> typeParameters() {
        return TYPE_PARAMETERS;
    }

    @Override
    public boolean _isSubType(TypeComparisonContext ctx, Type that) {
        ctx.putSubstituted(this, that);
        for (FunctionType member : functions()) {
            if (!ts.isFunctionDefined(ctx, member)) {
                return false;
            }
        }
        return true;
    }
}
