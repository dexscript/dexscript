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
import java.util.Map;

public class JavaType implements NamedType, FunctionsType, GenericType {

    private final OutShim oShim;
    private final Class clazz;
    private Map<TypeVariable, DType> jTypeVars;
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
        JavaTypes javaTypes = oShim.javaTypes();
        DType ret = javaTypes.resolve(method.getReturnType());
        if (ret == null) {
            return;
        }
        ArrayList<DType> params = new ArrayList<>();
        params.add(this);
        for (Class<?> paramClazz : method.getParameterTypes()) {
            DType param = javaTypes.resolve(paramClazz);
            if (param == null) {
                return;
            }
            params.add(param);
        }
        FunctionType function = new FunctionType(ts, method.getName(), params, ret);
        function.setImplProvider(expandedFunc -> new CallJavaMethod(oShim, expandedFunc, method));
        collector.add(function);
    }
//
//    private DType resolveJavaType(java.lang.reflect.Type javaType) {
//        if (javaType instanceof Class) {
//            return oShim.javaTypes().tryResolve((Class) javaType);
//        }
//        if (javaType instanceof TypeVariable) {
//            TypeVariable typeVar = (TypeVariable) javaType;
//            return javaTypeVarMap().get(typeVar);
//        }
//        return null;
//    }
//
//    private void newFuncs(List<FunctionType> collector) {
//        List<PlaceholderType> newFuncTypeParams = new ArrayList<>();
//        for (TypeVariable javaTypeVar : clazz.getTypeParameters()) {
//            DType typeParam = translateBound(javaTypeVar.getBounds());
//            newFuncTypeParams.add(new PlaceholderType(ts, javaTypeVar.getName(), typeParam));
//        }
//        String subClassName = null;
//        for (Constructor ctor : clazz.getConstructors()) {
//            if (subClassName == null) {
//                if (dTypeArgs == null) {
//                    subClassName = clazz.getCanonicalName();
//                } else {
//                    subClassName = oShim.genSubClass(clazz);
//                    oShim.javaTypes().add(subClassName, this);
//                }
//            }
//            newFunc(collector, ctor, subClassName, newFuncTypeParams);
//        }
//    }
//
//    private void newFunc(List<FunctionType> collector, Constructor ctor,
//                         String subClassName, List<PlaceholderType> newFuncTypeParams) {
//        ArrayList<DType> params = new ArrayList<>();
//        String funcName = clazz.getSimpleName();
//        params.add(new StringLiteralType(ts, funcName));
//        for (Class paramType : ctor.getParameterTypes()) {
//            DType type = oShim.javaTypes().tryResolve(paramType);
//            if (type == null) {
//                return;
//            }
//            params.add(type);
//        }
//        FunctionSig sig = new FunctionSig(ts, newFuncTypeParams, params, this, createRetElem(newFuncTypeParams));
//        FunctionType function = new FunctionType(ts, "New__", params, this, sig);
//        function.setImpl((FunctionType.LazyImpl) () -> new NewJavaClass(oShim, function, ctor, subClassName));
//        collector.add(function);
//    }
//
//    private DexType createRetElem(List<PlaceholderType> newFuncTypeParams) {
//        if (newFuncTypeParams.isEmpty()) {
//            return null;
//        }
//        StringBuilder expand = new StringBuilder();
//        expand.append("");
//        expand.append('<');
//        for (int i = 0; i < newFuncTypeParams.size(); i++) {
//            if (i > 0) {
//                expand.append(", ");
//            }
//            String placeholder = newFuncTypeParams.get(i).name();
//            expand.append(placeholder);
//        }
//        expand.append('>');
//        DexType retElem = DexType.parse(expand.toString());
//        return retElem;
//    }
//
//    public List<DType> typeParameters() {
//        if (dTypeParams != null) {
//            return dTypeParams;
//        }
//        dTypeParams = new ArrayList<>();
//        for (TypeVariable javaTypeVar : clazz.getTypeParameters()) {
//            DType typeParam = translateBound(javaTypeVar.getBounds());
//            dTypeParams.add(typeParam);
//        }
//        return dTypeParams;
//    }
//
//    private Map<TypeVariable, DType> javaTypeVarMap() {
//        if (jTypeVars != null) {
//            return jTypeVars;
//        }
//        jTypeVars = new HashMap<>();
//        List<DType> typeArgs = this.dTypeArgs;
//        if (typeArgs == null) {
//            typeArgs = typeParameters();
//        }
//        TypeVariable[] javaTypeVars = clazz.getTypeParameters();
//        for (int i = 0; i < typeArgs.size(); i++) {
//            jTypeVars.put(javaTypeVars[i], typeArgs.get(i));
//        }
//        return jTypeVars;
//    }

    private DType translateBound(java.lang.reflect.Type[] bounds) {
        // TODO: translate bound
        return ts.ANY;
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
            DType dTypeParam = ResolveType.$(ts, null, DexType.parse(dTypeParamSrc));
            dTypeParams.add(dTypeParam);
        }
        return dTypeParams;
    }
}
