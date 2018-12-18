package com.dexscript.transpile.type.actor;


import com.dexscript.type.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PromiseType implements NamedType, FunctionsType, GenericType {

    private final @NotNull FunctionType consumeFunc;
    private final TypeSystem ts;

    public PromiseType(TypeSystem ts) {
        this(ts, null);
    }

    public PromiseType(TypeSystem ts, List<DType> typeArgs) {
        this.ts = ts;
        if (typeArgs == null) {
            ts.defineType(this);
        }
        ts.lazyDefineFunctions(this);
        consumeFunc = consumeFunc(typeArgs == null ? Arrays.asList(ts.ANY) : typeArgs);
    }

    @Override
    public @NotNull String name() {
        return "Promise";
    }

    @NotNull
    private FunctionType consumeFunc(List<DType> typeArgs) {
        ArrayList<DType> params = new ArrayList<>();
        params.add(this);
        return new FunctionType(ts, "Consume__", params, typeArgs.get(0));
    }

    @Override
    public List<FunctionType> functions() {
        return Arrays.asList(consumeFunc);
    }

    @Override
    public DType generateType(List<DType> typeArgs) {
        return new PromiseType(ts, typeArgs);
    }

    @Override
    public List<DType> typeParameters() {
        return Arrays.asList(ts.ANY);
    }

    @Override
    public boolean _isAssignable(IsAssignable ctx, DType that) {
        return ts.functionTable().isSubType(ctx, this, that);
    }

    @Override
    public TypeSystem typeSystem() {
        return ts;
    }

    @Override
    public String toString() {
        return "Promise";
    }
}
