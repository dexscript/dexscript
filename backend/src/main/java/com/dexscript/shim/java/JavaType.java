package com.dexscript.shim.java;

import com.dexscript.ast.DexPackage;
import com.dexscript.ast.elem.DexSig;
import com.dexscript.shim.OutShim;
import com.dexscript.type.*;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
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
    private TypeSystem ts;
    private String description;
    private DexPackage dPkg;

    public JavaType(OutShim oShim, Class clazz) {
        this(oShim, clazz, null);
    }

    public JavaType(OutShim oShim, Class clazz, List<DType> dTypeArgs) {
        this.oShim = oShim;
        this.clazz = clazz;
        this.ts = oShim.typeSystem();
        dPkg = oShim.pkg(clazz);
        dTypeParams = new ArrayList<>();
        for (int i = 0; i < clazz.getTypeParameters().length; i++) {
            dTypeParams.add(ts.ANY);
        }
        if (dTypeArgs == null) {
            oShim.javaTypes().add(clazz, this);
            ts.defineType(this);
            this.dTypeArgs = dTypeParams;
        } else {
            this.dTypeArgs = dTypeArgs;
        }
        ts.lazyDefineFunctions(this);
    }

    @Override
    public List<FunctionType> functions() {
        if (functions != null) {
            return functions;
        }
        functions = new ArrayList<>();
        for (Method method : clazz.getMethods()) {
//            if (method.getDeclaringClass().equals(Object.class)) {
//                continue;
//            }
            javaMethodToDexFunc(functions, method);
        }
//        arrayGetToDexFunc(functions);
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
        func.implProvider(expandedFunc -> new CallJavaArrayGet(oShim, expandedFunc, clazz));
        collector.add(func);
    }

    private void javaMethodToDexFunc(List<FunctionType> collector, Method method) {
        DexSig sig = TranslateJavaMethod.$(oShim.javaTypes(), clazz, method);
        sig.attach(pkg());
        FunctionType func = new FunctionType(ts, method.getName(), localTypeTable(), sig);
        func.implProvider(expandedFunc -> new CallJavaMethod(oShim, expandedFunc, method));
        collector.add(func);
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
        String className = clazz.getCanonicalName();
        int dotPos = className.lastIndexOf('.');
        if (dotPos != -1) {
            className = className.substring(dotPos + 1);
        }
        return className
                .replace('$', '_')
                .replace("[]", "_array");
    }

    @Override
    public DexPackage pkg() {
        return dPkg;
    }

    @Override
    public DType generateType(List<DType> typeArgs) {
        return new JavaType(oShim, clazz, typeArgs);
    }

    @Override
    public List<DType> typeParameters() {
        if (dTypeParams != null) {
            return dTypeParams;
        }
        dTypeParams = new ArrayList<>();
        for (int i = 0; i < clazz.getTypeParameters().length; i++) {
            dTypeParams.add(ts.ANY);
        }
        return dTypeParams;
    }

    private TypeTable localTypeTable() {
        if (localTypeTable != null) {
            return localTypeTable;
        }
        localTypeTable = new TypeTable(ts);
        for (int i = 0; i < clazz.getTypeParameters().length; i++) {
            TypeVariable jTypeParam = clazz.getTypeParameters()[i];
            localTypeTable.define(oShim.pkg(clazz), jTypeParam.getName(), dTypeArgs.get(i));
        }
        return localTypeTable;
    }
}
