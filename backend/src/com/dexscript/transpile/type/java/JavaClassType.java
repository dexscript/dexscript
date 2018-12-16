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

public class JavaClassType implements NamedType, FunctionsProvider, GenericType {

    private final OutShim oShim;
    private final Class clazz;
    private final List<DType> typeArgs;
    private Map<TypeVariable, DType> javaTypeVarMap;
    private List<FunctionType> functions;
    private List<DType> typeParams;
    private ArrayList<PlaceholderType> placeholders;
    private TypeSystem ts;
    private String description;

    public JavaClassType(OutShim oShim, Class clazz) {
        this(oShim, clazz, null);
    }

    public JavaClassType(OutShim oShim, Class clazz, List<DType> typeArgs) {
        this.oShim = oShim;
        this.clazz = clazz;
        this.typeArgs = typeArgs;
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
        function.attach((FunctionType.LazyAttachment) () -> new CallJavaMethod(oShim, function, method));
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
        String subClassName = null;
        for (Constructor ctor : clazz.getConstructors()) {
            if (subClassName == null) {
                if (typeArgs == null) {
                    subClassName = clazz.getCanonicalName();
                } else {
                    subClassName = oShim.genSubClass(clazz);
                    oShim.javaTypes().add(subClassName, this);
                }
            }
            newFunc(collector, ctor, subClassName);
        }
    }

    private void newFunc(List<FunctionType> collector, Constructor ctor, String subClassName) {
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
        FunctionSig sig = new FunctionSig(ts, placeholders, params, this, createRetElem());
        FunctionType function = new FunctionType(ts, "New__", params, this, sig);
        function.attach((FunctionType.LazyAttachment) () -> new NewJavaClass(oShim, function, ctor, subClassName));
        collector.add(function);
    }

    private DexType createRetElem() {
        if (placeholders.isEmpty()) {
            return null;
        }
        StringBuilder expand = new StringBuilder();
        expand.append(name());
        expand.append('<');
        for (int i = 0; i < placeholders.size(); i++) {
            if (i > 0) {
                expand.append(", ");
            }
            String placeholder = placeholders.get(i).name();
            expand.append(placeholder);
        }
        expand.append('>');
        DexType retElem = DexType.parse(expand.toString());
        return retElem;
    }

    @Override
    public DType generateType(List<DType> typeArgs) {
        return new JavaClassType(oShim, clazz, typeArgs);
    }

    @Override
    public List<DType> typeParameters() {
        if (typeParams != null) {
            return typeParams;
        }
        typeParams = new ArrayList<>();
        placeholders = new ArrayList<>();
        for (TypeVariable javaTypeVar : clazz.getTypeParameters()) {
            DType typeParam = translateBound(javaTypeVar.getBounds());
            typeParams.add(typeParam);
            placeholders.add(new PlaceholderType(ts, javaTypeVar.getName(), typeParam));
        }
        return typeParams;
    }

    private Map<TypeVariable, DType> javaTypeVarMap() {
        if (javaTypeVarMap != null) {
            return javaTypeVarMap;
        }
        javaTypeVarMap = new HashMap<>();
        List<DType> typeArgs = this.typeArgs;
        if (typeArgs == null) {
            typeArgs = typeParameters();
        }
        TypeVariable[] javaTypeVars = clazz.getTypeParameters();
        for (int i = 0; i < typeArgs.size(); i++) {
            javaTypeVarMap.put(javaTypeVars[i], typeArgs.get(i));
        }
        return javaTypeVarMap;
    }

    private DType translateBound(java.lang.reflect.Type[] bounds) {
        // TODO: translate bound
        return ts.ANY;
    }

    @Override
    public boolean _isSubType(TypeComparisonContext ctx, DType that) {
        return ts.functionTable().isSubType(ctx, this, that);
    }

    @Override
    public TypeSystem typeSystem() {
        return ts;
    }

    @Override
    public String toString() {
        if (description == null) {
            description = describe(typeArgs);
        }
        return description;
    }
}
