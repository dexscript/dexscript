package com.dexscript.transpile.type.java;

import com.dexscript.runtime.DexRuntimeException;
import com.dexscript.transpile.shim.OutShim;
import com.dexscript.type.*;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.List;

public class JavaClassType implements NamedType, FunctionsProvider, GenericType {

    private final OutShim oShim;
    private final Class clazz;
    private final List<Type> typeArgs;
    private List<FunctionType> functions;
    private List<Type> typeParams;
    private ArrayList<PlaceholderType> placeholders;

    public JavaClassType(OutShim oShim, Class clazz) {
        this(oShim, clazz, null);
    }

    public JavaClassType(OutShim oShim, Class clazz, List<Type> typeArgs) {
        this.oShim = oShim;
        this.clazz = clazz;
        this.typeArgs = typeArgs;
        oShim.typeSystem().defineType(this);
        oShim.typeSystem().lazyDefineFunctions(this);
    }

    @Override
    public @NotNull String name() {
        return clazz.getSimpleName();
    }

    @Override
    public String toString() {
        return name();
    }

    @Override
    public boolean _isSubType(TypeComparisonContext ctx, Type that) {
        return false;
    }

    @Override
    public List<FunctionType> functions() {
        if (functions != null) {
            return functions;
        }
        functions = new ArrayList<>();
        newFuncs(functions);
        return functions;
    }

    private void newFuncs(List<FunctionType> functions) {
        for (Constructor ctor : clazz.getConstructors()) {
            FunctionType function = newFunc(ctor);
            if (function != null) {
                functions.add(function);
            }
        }
    }

    private FunctionType newFunc(Constructor ctor) {
        ArrayList<Type> params = new ArrayList<>();
        String funcName = clazz.getSimpleName();
        params.add(new StringLiteralType(funcName));
        for (Class paramType : ctor.getParameterTypes()) {
            Type type = oShim.javaTypes().tryResolve(paramType);
            if (type == null) {
                return null;
            }
            params.add(type);
        }
        FunctionSig sig = new FunctionSig(placeholders, params, this, null);
        FunctionType function = new FunctionType("New__", params, this, sig);
        function.attach((FunctionType.LazyAttachment) () -> new NewJavaClass(oShim, function, ctor));
        return function;
    }

    @Override
    public Type generateType(List<Type> typeArgs) {
        return new JavaClassType(oShim, clazz, typeArgs);
    }

    @Override
    public List<Type> typeParameters() {
        if (typeParams != null) {
            return typeParams;
        }
        typeParams = new ArrayList<>();
        for (TypeVariable typeParameter : clazz.getTypeParameters()) {
            typeParams.add(BuiltinTypes.ANY);
        }
        placeholders = new ArrayList<>();
        for (Type typeParam : typeParameters()) {
            placeholders.add(new PlaceholderType("T", typeParam));
        }
        return typeParams;
    }
}
