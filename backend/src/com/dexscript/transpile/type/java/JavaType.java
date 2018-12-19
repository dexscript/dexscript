package com.dexscript.transpile.type.java;

import com.dexscript.ast.type.DexType;
import com.dexscript.transpile.shim.OutShim;
import com.dexscript.type.*;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
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
    private TypeSystem ts;
    private String description;

    public JavaType(OutShim oShim, Class clazz) {
        this(oShim, clazz, null);
    }

    public JavaType(OutShim oShim, Class clazz, List<DType> dTypeArgs) {
        this.oShim = oShim;
        this.clazz = clazz;
        this.ts = oShim.typeSystem();
        this.dTypeArgs = dTypeArgs;
        if (dTypeArgs == null) {
            ts.defineType(this);
        }
        oShim.javaTypes().add(clazz, this);
        ts.lazyDefineFunctions(this);
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
        Type jRet = method.getGenericReturnType();
        DType dRet = resolve(jRet);
        if (dRet == null) {
            return;
        }
        ArrayList<DType> dParams = new ArrayList<>();
        dParams.add(this);
        for (Type jParam : method.getGenericParameterTypes()) {
            DType dParam = resolve(jParam);
            if (dParam == null) {
                return;
            }
            dParams.add(dParam);
        }
        FunctionType function = new FunctionType(ts, method.getName(), dParams, dRet);
        function.setImplProvider(expandedFunc -> new CallJavaMethod(oShim, expandedFunc, method));
        collector.add(function);
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
        return new JavaType(oShim, clazz, typeArgs);
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
            String dTypeParamSrc = TranslateSig.translateType(javaTypes, jTypeParams[0]);
            // TODO: implement recursive generic expansion
            if (dTypeParamSrc.startsWith(name())) {
                dTypeParams.add(ts.ANY);
            } else {
                DType dTypeParam = ResolveType.$(ts, null, DexType.parse(dTypeParamSrc));
                dTypeParams.add(dTypeParam);
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
