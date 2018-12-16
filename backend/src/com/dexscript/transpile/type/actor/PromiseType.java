package com.dexscript.transpile.type.actor;


import com.dexscript.type.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PromiseType implements NamedType, FunctionsProvider, GenericType {

    private final static List<DType> TYPE_PARAMETERS = Arrays.asList(BuiltinTypes.ANY);
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
        consumeFunc = consumeFunc(typeArgs == null ? TYPE_PARAMETERS : typeArgs);
    }

    @Override
    public @NotNull String name() {
        return "Promise";
    }

    @NotNull
    private FunctionType consumeFunc(List<DType> typeArgs) {
        ArrayList<DType> params = new ArrayList<>();
        params.add(this);
        return new FunctionType("Consume__", params, typeArgs.get(0));
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
        return TYPE_PARAMETERS;
    }

    @Override
    public boolean _isSubType(TypeComparisonContext ctx, DType that) {
        return ts.isSubType(ctx, this, that);
    }

    @Override
    public String toString() {
        return "Promise";
    }
}
