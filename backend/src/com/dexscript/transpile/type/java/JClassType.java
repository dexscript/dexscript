package com.dexscript.transpile.type.java;

import com.dexscript.ast.type.DexType;
import com.dexscript.transpile.shim.OutShim;
import com.dexscript.transpile.type.JavaTypes;
import com.dexscript.type.*;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JClassType implements NamedType, FunctionsType, GenericType {

    private final OutShim oShim;
    private final Class clazz;
    private final List<DType> dTypeArgs;
    private Map<TypeVariable, DType> jTypeVars;
    private List<FunctionType> functions;
    private List<DType> dTypeParams;
    private TypeSystem ts;
    private String description;

    public JClassType(OutShim oShim, Class clazz) {
        this(oShim, clazz, null);
    }

    public JClassType(OutShim oShim, Class clazz, List<DType> typeArgs) {
        this.oShim = oShim;
        this.clazz = clazz;
        this.dTypeArgs = typeArgs;
        this.ts = oShim.typeSystem();
        if (typeArgs == null) {
            oShim.javaTypes().add(clazz, this);
            ts.defineType(this);
        }
        ts.lazyDefineFunctions(this);
    }

    @Override
    public @NotNull String name() {
        return clazz.getSimpleName();
    }

    @Override
    public List<FunctionType> functions() {
        if (functions != null) {
            return functions;
        }
        typeParameters(); // calculate placeholders
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
        function.setImpl((FunctionType.LazyImpl) () -> new CallJavaMethod(oShim, function, method));
        collector.add(function);
    }

    private DType resolveJavaType(java.lang.reflect.Type javaType) {
        if (javaType instanceof Class) {
            return oShim.javaTypes().tryResolve((Class) javaType);
        }
        if (javaType instanceof TypeVariable) {
            TypeVariable typeVar = (TypeVariable) javaType;
            return javaTypeVarMap().get(typeVar);
        }
        return null;
    }

    private void newFuncs(List<FunctionType> collector) {
        List<PlaceholderType> newFuncTypeParams = new ArrayList<>();
        for (TypeVariable javaTypeVar : clazz.getTypeParameters()) {
            DType typeParam = translateBound(javaTypeVar.getBounds());
            newFuncTypeParams.add(new PlaceholderType(ts, javaTypeVar.getName(), typeParam));
        }
        String subClassName = null;
        for (Constructor ctor : clazz.getConstructors()) {
            if (subClassName == null) {
                if (dTypeArgs == null) {
                    subClassName = clazz.getCanonicalName();
                } else {
                    subClassName = oShim.genSubClass(clazz);
                    oShim.javaTypes().add(subClassName, this);
                }
            }
            newFunc(collector, ctor, subClassName, newFuncTypeParams);
        }
    }

    private void newFunc(List<FunctionType> collector, Constructor ctor,
                         String subClassName, List<PlaceholderType> newFuncTypeParams) {
        ArrayList<DType> params = new ArrayList<>();
        String funcName = clazz.getSimpleName();
        params.add(new StringLiteralType(ts, funcName));
        for (Class paramType : ctor.getParameterTypes()) {
            DType type = oShim.javaTypes().tryResolve(paramType);
            if (type == null) {
                return;
            }
            params.add(type);
        }
        FunctionSig sig = new FunctionSig(ts, newFuncTypeParams, params, this, createRetElem(newFuncTypeParams));
        FunctionType function = new FunctionType(ts, "New__", params, this, sig);
        function.setImpl((FunctionType.LazyImpl) () -> new NewJavaClass(oShim, function, ctor, subClassName));
        collector.add(function);
    }

    private DexType createRetElem(List<PlaceholderType> newFuncTypeParams) {
        if (newFuncTypeParams.isEmpty()) {
            return null;
        }
        StringBuilder expand = new StringBuilder();
        expand.append(name());
        expand.append('<');
        for (int i = 0; i < newFuncTypeParams.size(); i++) {
            if (i > 0) {
                expand.append(", ");
            }
            String placeholder = newFuncTypeParams.get(i).name();
            expand.append(placeholder);
        }
        expand.append('>');
        DexType retElem = DexType.parse(expand.toString());
        return retElem;
    }

    @Override
    public DType generateType(List<DType> typeArgs) {
        return new JClassType(oShim, clazz, typeArgs);
    }

    @Override
    public List<DType> typeParameters() {
        if (dTypeParams != null) {
            return dTypeParams;
        }
        dTypeParams = new ArrayList<>();
        for (TypeVariable javaTypeVar : clazz.getTypeParameters()) {
            DType typeParam = translateBound(javaTypeVar.getBounds());
            dTypeParams.add(typeParam);
        }
        return dTypeParams;
    }

    private Map<TypeVariable, DType> javaTypeVarMap() {
        if (jTypeVars != null) {
            return jTypeVars;
        }
        jTypeVars = new HashMap<>();
        List<DType> typeArgs = this.dTypeArgs;
        if (typeArgs == null) {
            typeArgs = typeParameters();
        }
        TypeVariable[] javaTypeVars = clazz.getTypeParameters();
        for (int i = 0; i < typeArgs.size(); i++) {
            jTypeVars.put(javaTypeVars[i], typeArgs.get(i));
        }
        return jTypeVars;
    }

    private DType translateBound(java.lang.reflect.Type[] bounds) {
        // TODO: translate bound
        return ts.ANY;
    }

    @Override
    public boolean _isSubType(TypeComparisonContext ctx, DType that) {
        if (clazz.isInterface()) {
            return ts.functionTable().isSubType(ctx, this, that);
        } else {
            return this.equals(that);
        }
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
}
