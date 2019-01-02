package com.dexscript.shim.java;

import com.dexscript.ast.DexPackage;
import com.dexscript.shim.OutShim;
import com.dexscript.type.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArrayType implements NamedType, GenericType, FunctionsType {

    private final TypeSystem ts;
    private final OutShim oShim;
    private final List<DType> typeArgs;
    private ArrayList<FunctionType> functions;

    public ArrayType(OutShim oShim) {
        this(oShim, null);
    }

    public ArrayType(OutShim oShim, List<DType> typeArgs) {
        this.oShim = oShim;
        this.ts = oShim.typeSystem();
        if (typeArgs == null) {
            ts.defineType(this);
            this.typeArgs = typeParameters();
        } else {
            this.typeArgs = typeArgs;
        }
        oShim.javaTypes().add(Object[].class, this);
    }

    @Override
    public @NotNull String name() {
        return "Array";
    }

    @Override
    public DexPackage pkg() {
        return DexPackage.BUILTIN;
    }

    @Override
    public boolean _isAssignable(IsAssignable ctx, DType that) {
        return this.equals(that);
    }

    @Override
    public TypeSystem typeSystem() {
        return ts;
    }

    @Override
    public DType generateType(List<DType> typeArgs) {
        return new ArrayType(oShim, typeArgs);
    }

    @Override
    public List<DType> typeParameters() {
        return Arrays.asList(ts.ANY);
    }

    @Override
    public String toString() {
        return "Array<" + typeArgs.get(0) + ">";
    }

    @Override
    public List<FunctionType> functions() {
        if (functions != null) {
            return functions;
        }
        functions = new ArrayList<>();
        functions.add(arrayGetFunc());
        functions.add(arraySetFunc());
        return functions;
    }

    @NotNull
    private FunctionType arraySetFunc() {
        ArrayList<FunctionParam> params = new ArrayList<>();
        params.add(new FunctionParam("self", this));
        params.add(new FunctionParam("index", ts.INT32));
        params.add(new FunctionParam("element", typeArgs.get(0)));
        FunctionType func = new FunctionType(ts, "Set__", params, ts.VOID);
        func.implProvider(expandedFunc -> new CallJavaArraySet(oShim, expandedFunc, Object[].class));
        return func;
    }

    @NotNull
    private FunctionType arrayGetFunc() {
        ArrayList<FunctionParam> params = new ArrayList<>();
        params.add(new FunctionParam("self", this));
        params.add(new FunctionParam("index", ts.INT32));
        DType ret = typeArgs.get(0);
        FunctionType func = new FunctionType(ts, "Get__", params, ret);
        func.implProvider(expandedFunc -> new CallJavaArrayGet(oShim, expandedFunc, Object[].class));
        return func;
    }
}
