package com.dexscript.shim.java;

import com.dexscript.ast.type.DexType;
import com.dexscript.shim.OutShim;
import com.dexscript.type.*;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.List;

public class JavaType implements NamedType, FunctionsType, GenericType {

    private final OutShim oShim;
    private final Class clazz;
    private TypeTable localTypeTable;
    private List<FunctionType> functions;
    private List<DType> dTypeParams;
    private List<DType> dTypeArgs;
    private final String runtimeClassName;
    private TypeSystem ts;
    private String description;

    public JavaType(OutShim oShim, Class clazz) {
        this(oShim, clazz, null, clazz.getCanonicalName());
    }

    public JavaType(OutShim oShim, Class clazz, List<DType> dTypeArgs, String runtimeClassName) {
        this.oShim = oShim;
        this.clazz = clazz;
        this.ts = oShim.typeSystem();
        this.dTypeArgs = dTypeArgs;
        this.runtimeClassName = runtimeClassName;
        if (dTypeArgs == null) {
            ts.defineType(this);
        }
        oShim.javaTypes().add(runtimeClassName, this);
        ts.lazyDefineFunctions(this);
    }

    public String runtimeClassName() {
        return runtimeClassName;
    }

    @Override
    public List<FunctionType> functions() {
        if (functions != null) {
            return functions;
        }
        functions = new ArrayList<>();
        for (Method method : clazz.getMethods()) {
            javaMethodToDexFunc(functions, method);
        }
        arrayGetToDexFunc(functions);
        return functions;
    }

    private void arrayGetToDexFunc(List<FunctionType> collector) {
        if (!clazz.isArray()) {
            return;
        }
        ArrayList<FunctionParam> params = new ArrayList<>();
        params.add(new FunctionParam("self", this));
        params.add(new FunctionParam("index", ts.INT32));
        DType ret = oShim.javaTypes().resolve(clazz.getComponentType());
        FunctionType func = new FunctionType(ts, "get", params, ret);
        func.setImplProvider(expandedFunc -> new CallJavaArrayGet(oShim, expandedFunc, clazz));
        collector.add(func);
    }

    private void javaMethodToDexFunc(List<FunctionType> collector, Method method) {
        Type jRet = method.getGenericReturnType();
        DType dRet = resolve(jRet);
        if (dRet == null) {
            return;
        }
        List<FunctionParam> dParams = new ArrayList<>();
        dParams.add(new FunctionParam("self", this));
        for (Parameter jParam : method.getParameters()) {
            DType dParam = resolve(jParam.getType());
            if (dParam == null) {
                return;
            }
            dParams.add(new FunctionParam(jParam.getName(), dParam));
        }
        FunctionType func = new FunctionType(ts, method.getName(), dParams, dRet);
        func.setImplProvider(expandedFunc -> new CallJavaMethod(oShim, expandedFunc, method));
        collector.add(func);
    }

    private DType resolve(Type jTypeObj) {
        String src = TranslateSig.translateType(oShim.javaTypes(), jTypeObj);
        TypeTable localTypeTable = localTypeTable();
        return ResolveType.$(ts, localTypeTable, DexType.parse(src));
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
    public String toString() {
        if (description == null) {
            description = describe(dTypeArgs);
        }
        return description;
    }

    @Override
    public @NotNull String name() {
        return TranslateSig.dTypeNameOf(clazz);
    }

    @Override
    public DType generateType(List<DType> typeArgs) {
        String runtimeClassName = oShim.genSubClass(clazz);
        return new JavaType(oShim, clazz, typeArgs, runtimeClassName);
    }

    @Override
    public List<DType> typeParameters() {
        if (dTypeParams != null) {
            return dTypeParams;
        }
        dTypeParams = new ArrayList<>();
        JavaTypes javaTypes = oShim.javaTypes();
        for (TypeVariable jTypeVar : clazz.getTypeParameters()) {
            Type[] jTypeParams = jTypeVar.getBounds();
            if (jTypeParams.length != 1) {
                throw new UnsupportedOperationException("not implemented");
            }
            // TODO: implement recursive generic expansion
            Type jTypeParam = jTypeParams[0];
            if (jTypeParam instanceof Class) {
                dTypeParams.add(javaTypes.resolve((Class) jTypeParam));
            } else {
                dTypeParams.add(ts.ANY);
            }
        }
        return dTypeParams;
    }

    private TypeTable localTypeTable() {
        if (localTypeTable != null) {
            return localTypeTable;
        }
        localTypeTable = new TypeTable(ts);
        List<DType> dTypeArgs = this.dTypeArgs;
        if (dTypeArgs == null) {
            dTypeArgs = typeParameters();
        }
        for (int i = 0; i < clazz.getTypeParameters().length; i++) {
            TypeVariable jTypeParam = clazz.getTypeParameters()[i];
            localTypeTable.define(jTypeParam.getName(), dTypeArgs.get(i));
        }
        return localTypeTable;
    }
}
