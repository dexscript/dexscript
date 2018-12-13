package com.dexscript.transpile.type;


import com.dexscript.type.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PromiseType implements NamedType, FunctionsProvider, GenericType {

    private final static List<Type> TYPE_PARAMETERS = Arrays.asList(BuiltinTypes.ANY);
    private final @NotNull FunctionType consumeFunc;
    private final TypeSystem ts;

    public PromiseType(TypeSystem ts) {
        this(ts, null);
    }

    public PromiseType(TypeSystem ts, List<Type> typeArgs) {
        this.ts = ts;
        if (typeArgs == null) {
            ts.defineType(this);
        }
        ts.lazyDefineFunctions(this);
        consumeFunc = consumeFunc(typeArgs == null ? TYPE_PARAMETERS : typeArgs);
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
        return new PromiseType(ts, typeArgs);
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
