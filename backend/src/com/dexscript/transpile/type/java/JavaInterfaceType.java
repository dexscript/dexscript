package com.dexscript.transpile.type.java;

import com.dexscript.transpile.shim.OutShim;
import com.dexscript.type.*;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JavaInterfaceType implements GenericType, FunctionsType {

    private final OutShim oShim;
    private final Class clazz;
    private Map<TypeVariable, DType> jTypeVars;
    private List<FunctionType> functions;
    private List<DType> dTypeParams;
    private TypeSystem ts;

    public JavaInterfaceType(OutShim oShim, Class clazz) {
        this.oShim = oShim;
        this.clazz = clazz;
        this.ts = oShim.typeSystem();
        ts.defineType(this);
        ts.lazyDefineFunctions(this);
    }

    @Override
    public DType generateType(List<DType> typeArgs) {
        return null;
    }

    @Override
    public List<DType> typeParameters() {
        return null;
    }

    @Override
    public @NotNull String name() {
        return clazz.getSimpleName();
    }

    @Override
    public boolean _isAssignable(IsAssignable ctx, DType that) {
        return ts.functionTable().isAssignable(ctx, this, that);
    }

    @Override
    public TypeSystem typeSystem() {
        return ts;
    }

    @Override
    public List<FunctionType> functions() {
        if (functions != null) {
            return functions;
        }
        functions = new ArrayList<>();
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
        DType ret = resolveJavaType(method.getGenericReturnType());
        if (ret == null) {
            return;
        }
        ArrayList<DType> params = new ArrayList<>();
        params.add(this);
        for (Class<?> paramClazz : method.getParameterTypes()) {
            DType param = javaTypes.tryResolve(paramClazz);
            if (param == null) {
                return;
            }
            params.add(param);
        }
        FunctionType function = new FunctionType(ts, method.getName(), params, ret);
        collector.add(function);
    }

    private DType resolveJavaType(java.lang.reflect.Type javaType) {
        if (javaType instanceof Class) {
            return oShim.javaTypes().tryResolve((Class) javaType);
        }
        return null;
    }

    @Override
    public String toString() {
        return name();
    }
}
