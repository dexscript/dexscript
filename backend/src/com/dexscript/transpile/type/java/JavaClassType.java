package com.dexscript.transpile.type.java;

import com.dexscript.transpile.shim.OutShim;
import com.dexscript.transpile.type.JavaTypes;
import com.dexscript.type.*;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
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
    private TypeSystem ts;

    public JavaClassType(OutShim oShim, Class clazz) {
        this(oShim, clazz, null);
    }

    public JavaClassType(OutShim oShim, Class clazz, List<Type> typeArgs) {
        this.oShim = oShim;
        this.clazz = clazz;
        this.typeArgs = typeArgs;
        ts = oShim.typeSystem();
        oShim.javaTypes().add(clazz, this);
        ts.defineType(this);
        ts.lazyDefineFunctions(this);
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
    public List<FunctionType> functions() {
        if (functions != null) {
            return functions;
        }
        functions = new ArrayList<>();
        newFuncs(functions);
        javaMethodToDexFuncs(functions);
        return functions;
    }

    private void javaMethodToDexFuncs(List<FunctionType> collector) {
        for (Method method : clazz.getMethods()) {
            javaMethodToDexFunc(collector, method);
        }
    }

    private void javaMethodToDexFunc(List<FunctionType> collector, Method method) {
        JavaTypes javaTypes = oShim.javaTypes();
        Type ret = javaTypes.tryResolve(method.getReturnType());
        if (ret == null) {
            return;
        }
        ArrayList<Type> params = new ArrayList<>();
        params.add(this);
        for (Class<?> paramClazz : method.getParameterTypes()) {
            Type param = javaTypes.tryResolve(paramClazz);
            if (param == null) {
                return;
            }
            params.add(param);
        }
        FunctionType function = new FunctionType(method.getName(), params, ret);
        function.attach((FunctionType.LazyAttachment) () -> new CallJavaMethod(oShim, function, method));
        collector.add(function);
    }

    private void newFuncs(List<FunctionType> collector) {
        for (Constructor ctor : clazz.getConstructors()) {
            newFunc(collector, ctor);
        }
    }

    private void newFunc(List<FunctionType> collector, Constructor ctor) {
        ArrayList<Type> params = new ArrayList<>();
        String funcName = clazz.getSimpleName();
        params.add(new StringLiteralType(funcName));
        for (Class paramType : ctor.getParameterTypes()) {
            Type type = oShim.javaTypes().tryResolve(paramType);
            if (type == null) {
                return;
            }
            params.add(type);
        }
        FunctionSig sig = new FunctionSig(placeholders, params, this, null);
        FunctionType function = new FunctionType("New__", params, this, sig);
        function.attach((FunctionType.LazyAttachment) () -> new NewJavaClass(oShim, function, ctor));
        collector.add(function);
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

    @Override
    public boolean _isSubType(TypeComparisonContext ctx, Type that) {
        return ts.isSubType(ctx, this, that);
    }
}
